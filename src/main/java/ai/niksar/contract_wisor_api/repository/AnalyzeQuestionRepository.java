package ai.niksar.contract_wisor_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.dto.AnalyzeQuestionDTO;
import ai.niksar.contract_wisor_api.model.AnalyzeQuestion;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyzeQuestionRepository extends JpaRepository<AnalyzeQuestion, UUID> {
    @Query("SELECT q FROM AnalyzeQuestion q WHERE q.code = :code")
    AnalyzeQuestion findByCode(@PathVariable String code);
    @Query("SELECT new ai.niksar.contract_wisor_api.dto.AnalyzeQuestionDTO(" +
            "q.id, q.code, q.questionDesc, q.questionTitle) " +
            "FROM AnalyzeQuestion q ")
    List<AnalyzeQuestionDTO> listQuestionDTO();
}
