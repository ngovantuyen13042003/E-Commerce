package nvt.st.accounts.service;

import nvt.st.accounts.dto.CustomerDTO;

public interface IAccountService {

    void createAccount(CustomerDTO customerDTO);

    CustomerDTO fetchAccount(String phoneNumber);
}
