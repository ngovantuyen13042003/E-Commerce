package nvt.st.accounts.repository;

import jakarta.transaction.Transactional;
import nvt.st.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByCustomerId(Long id);

    @Transactional
    @Modifying
    void  deleteByCustomerId(Long id);
}
