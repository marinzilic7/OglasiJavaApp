package oglasi.repositories;
import oglasi.model.Oglasi;
import oglasi.model.Recenzije;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecenzijeRepository extends JpaRepository<Recenzije, Long> {
    List<Recenzije> findByOglasi(Oglasi oglasi);
}