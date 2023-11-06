package dk.kea.project.repository;

import dk.kea.project.entity.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Integer> {
}
