package nvt.st.accounts.service.impl;

import nvt.st.accounts.constants.AccountsConstants;
import nvt.st.accounts.dto.AccountDTO;
import nvt.st.accounts.dto.CustomerDTO;
import nvt.st.accounts.entity.Accounts;
import nvt.st.accounts.entity.Customer;
import nvt.st.accounts.exception.CustomerAlreadyExistsException;
import nvt.st.accounts.exception.ResourceNotFoundException;
import nvt.st.accounts.mapper.AccountMapper;
import nvt.st.accounts.mapper.CustomerMapper;
import nvt.st.accounts.repository.AccountRepository;
import nvt.st.accounts.repository.CustomerRepository;
import nvt.st.accounts.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public CustomerDTO fetchAccount(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "phoneNumber", phoneNumber)
        );

        Accounts accounts = accountRepository.findByCustomerId(customer.getId()).orElseThrow(
                ()-> new ResourceNotFoundException("Account", "customerId", customer.getId().toString())
        );

        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDTO( new CustomerDTO(), customer);
        customerDTO.setAccountDTO(AccountMapper.mapToAccountDTO( new AccountDTO(), accounts));

        return customerDTO;
    }

    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {
        boolean idUpdate = false;
        AccountDTO accountDTO = customerDTO.getAccountDTO();
        if(accountDTO != null) {
            Accounts accounts = accountRepository.findById(accountDTO.getAccountNumber()).orElseThrow(
                    ()-> new ResourceNotFoundException("Account", "AccountNumber", accountDTO.getAccountNumber().toString())
            );
            AccountMapper.mapToAccounts(accountDTO, accounts);
            accounts.setUpdatedAt(LocalDateTime.now());
            accounts.setUpdatedBy("Ngo Van Tuyen");
            accounts = accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new  ResourceNotFoundException("Customer", "CustomerId", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDTO, customer);
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setUpdatedBy("Ngo Van Tuyen");
            customerRepository.save(customer);
            idUpdate = true;
        }
        return idUpdate;
    }

    @Override
    public boolean deleteAccount(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "phoneNumber", phoneNumber)
        );

        accountRepository.deleteByCustomerId(customer.getId());
        customerRepository.deleteById(customer.getId());
        return true;
    }

    @Override
    public void createAccount(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer>  optionalCustomer = customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with give phone number "+ customerDTO.getPhoneNumber() );
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Ngo Van Tuyen");

        Customer saveCustomer= customerRepository.save(customer);
        accountRepository.save(createNewAccount(saveCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getId());
        Long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Ngo Van Tuyen");
        return newAccount;
    }


}