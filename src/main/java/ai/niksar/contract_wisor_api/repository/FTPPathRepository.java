package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.model.FTPPath;

import java.util.UUID;

@Repository
public interface FTPPathRepository extends JpaRepository<FTPPath, UUID> {
    @Query("SELECT f FROM FTPPath f WHERE f.longName = :longName ")
    FTPPath getFTPPathByLongName(@PathVariable String longName);
}