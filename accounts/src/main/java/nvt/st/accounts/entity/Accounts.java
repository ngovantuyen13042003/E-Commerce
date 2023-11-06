package nvt.st.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Accounts extends BaseEntity{
    @Id
    private Long accountNumber;

    private Long customerId;

    private String accountType;

    private String branchAddress;



}
