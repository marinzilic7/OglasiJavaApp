package course.shop.repositories;
import course.shop.model.Oglasi;
import course.shop.model.Recenzije;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecenzijeRepository extends JpaRepository<Recenzije, Long> {
    List<Recenzije> findByOglasi(Oglasi oglasi);
}