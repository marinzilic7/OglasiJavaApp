package oglasi.repositories;

import oglasi.model.Oglasi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OglasiRepository extends JpaRepository <Oglasi, Long> {}