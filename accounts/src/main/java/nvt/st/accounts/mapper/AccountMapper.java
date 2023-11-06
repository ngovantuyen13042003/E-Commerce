package nvt.st.accounts.mapper;

import nvt.st.accounts.dto.AccountDTO;
import nvt.st.accounts.entity.Accounts;

public class AccountMapper {
    public static Accounts mapToAccounts(AccountDTO accountDTO, Accounts accounts) {
        accounts.setAccountNumber(accountDTO.getAccountNumber());
        accounts.setAccountType(accountDTO.getAccountType());
        accounts.setBranchAddress(accountDTO.getBranchAddress());
        return accounts;
    }

    public static AccountDTO mapToAccountDTO(AccountDTO accountDTO, Accounts accounts) {
        accountDTO.setAccountNumber(accounts.getAccountNumber());
        accountDTO.setAccountType(accounts.getAccountType());
        accountDTO.setBranchAddress(accounts.getBranchAddress());
        return accountDTO;
    }
}
