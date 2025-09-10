package com.bankapp.customer_service.service;

import com.bankapp.customer_service.dto.*;
import com.bankapp.customer_service.feign.AccountServiceClient;
import com.bankapp.customer_service.feign.UserServiceClient;
import com.bankapp.customer_service.mapper.CustomerMapper;
import com.bankapp.customer_service.model.Customer;
import com.bankapp.customer_service.repository.CustomerRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private AccountServiceClient accountServiceClient;

    // CREATE CUSTOMER
    public CustomerResponseDTO createCustomer(CustomerCreateDTO createDTO) {
        Customer customer = customerMapper.toEntity(createDTO);
        customer.setRegistrationDate(LocalDate.now());
        customer.setActive(true);

        // GENERATING CUSTOM CUSTOMER-ID
        String customerId = generateCustomerId(customer);
        customer.setCustomerId(customerId);

        // ENCODING & SETTING PASSWORD
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        // SAVING IN CUSTOMER DB
        Customer saved = customerRepo.save(customer);

        // Create UserRegisterDTO and call user-service
        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setUserId(customerId);
        userDto.setPassword(customer.getPassword());
        userDto.setRole("CUSTOMER");

        userServiceClient.createUser(userDto); // sends data to user-service

        // 🔹 Create a default account for this customer (example: Savings Account)
        AccountCreateDTO accountDto = new AccountCreateDTO();
        accountDto.setCustomerId(customerId);
        accountDto.setAccountType(saved.getAccountType());
        accountDto.setStatus("ACTIVE");

        accountServiceClient.createAccount(accountDto);

        return customerMapper.toResponseDTO(saved);
    }


    /**
     * Retrieves a list of all customer records from the database.
     * This method is typically restricted to administrators.
     *
     * @return A list of CustomerResponseDTOs for all employees.
     */
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepo.findAll().stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    /**
     * Fetches the details of a single customer by their user ID.
     *
     * @param userId The unique ID of the customer.
     * @return An CustomerResponseDTO for the specified customer.
     * @throws EntityNotFoundException if the customer is not found.
     */
    public CustomerResponseDTO getCustomerDetails(String userId) {
        Customer customer = customerRepo.findByCustomerId(userId);
        if (customer == null) {
            throw new EntityNotFoundException("Customer not found with id: " + userId);
        }
        return customerMapper.toResponseDTO(customer);
    }


    /**
     * Updates an existing customer's details based on their ID.
     *
     * @param userId The unique ID of the customer.
     * @param customerUpdateDTO The DTO containing the updated customer information.
     * @return A CustomerResponseDTO of the updated customer.
     * @throws EntityNotFoundException if the customer is not found.
     */

    public CustomerResponseDTO updateCustomerDetails(String userId, CustomerUpdateDTO customerUpdateDTO) {
        Customer existingCustomer = customerRepo.findByCustomerId(userId);
        if (existingCustomer == null) {
            throw new EntityNotFoundException("Customer not found with id: " + userId);
        }

        customerMapper.updateCustomerFromDTO(customerUpdateDTO, existingCustomer);

        Customer updatedCustomer = customerRepo.save(existingCustomer);
        return customerMapper.toResponseDTO(updatedCustomer);
    }


    /**
     * Updates a customer's password based on their ID.
     * The new password is first encoded before being saved.
     *
     * @param userId The unique ID of the customer.
     * @param passwordDto The DTO containing the old and new passwords.
     * @return A CustomerResponseDTO of the updated customer.
     * @throws EntityNotFoundException if the customer is not found.
     * @throws SecurityException if the old password does not match.
     */

    public String updateCustomerPassword(String userId, CustomerPasswordUpdateDTO passwordDto) {
        Customer existingCustomer = customerRepo.findByCustomerId(userId);
        if (existingCustomer == null) {
            throw new EntityNotFoundException("Customer not found with id: " + userId);
        }

        // Validate the old password
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), existingCustomer.getPassword())) {
            throw new SecurityException("Old password does not match.");
        }

        try {
            // Hash the new password
            String encodedPassword = passwordEncoder.encode(passwordDto.getNewPassword());
            existingCustomer.setPassword(encodedPassword);

            // Update the password in the user-service via Feign client
            NewPasswordDTO newPasswordDTO = new NewPasswordDTO(encodedPassword);
            userServiceClient.updateUserPassword(userId, newPasswordDTO);

            // Save to local DB
            Customer updatedCustomer = customerRepo.save(existingCustomer);

            // Verify if update worked
            if (updatedCustomer != null && passwordEncoder.matches(passwordDto.getNewPassword(), updatedCustomer.getPassword())) {
                return "✅ Password Changed Successfully !!";
            } else {
                return "❌ Password change failed!";
            }

        } catch (Exception e) {
            // log error
            System.err.println("Error while changing password for user " + userId + ": " + e.getMessage());
            return "❌ Password change failed due to an internal error.";
        }
    }


    /**
     * Deletes a customer record from the database based on their ID.
     *
     * @param userId The unique ID of the customer to delete.
     * @throws EntityNotFoundException if the customer is not found.
     */

    public void deleteCustomer(String userId) {
        Customer customer = customerRepo.findByCustomerId(userId);
        if (customer == null) {
            throw new EntityNotFoundException("Customer not found with id: " + userId);
        }

        userServiceClient.deleteUser(userId);
        customerRepo.delete(customer);
    }

    // CUSTOMER ID GENERATION:
    private String generateCustomerId(Customer customer) {
        String firstTwoFirstName = customer.getFirstName().substring(0, 2).toUpperCase();
        String firstTwoLastName = customer.getLastName().substring(0, 2).toUpperCase();
        String birthYear = String.valueOf(customer.getDateOfBirth().getYear());
        String lastTwoBirthYear = birthYear.substring(birthYear.length() - 2);
        String lastTwoAadhar = customer.getAadharNumber().substring(customer.getAadharNumber().length() - 2);
        String lastTwoPhone = customer.getPhoneNumber().substring(customer.getPhoneNumber().length() - 2);

        return "C" + firstTwoFirstName + firstTwoLastName + lastTwoBirthYear + lastTwoAadhar + lastTwoPhone;
    }

}
