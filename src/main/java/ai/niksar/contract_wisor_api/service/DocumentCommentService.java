package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.model.DocumentComment;
import ai.niksar.contract_wisor_api.repository.DocumentCommentRepository;
import ai.niksar.contract_wisor_api.util.Util;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentCommentService {
    @Autowired
    private DocumentCommentRepository documentCommentRepository;

    public  DocumentComment saveDocumentComment(DocumentComment documentComment,UUID documentId){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        if(documentComment.getId() == null){
            documentComment.setCommentUser(username);
            documentComment.setCreateDate(Util.getRealDate());
            documentComment.setCreateTime(Util.getRealTime());
            documentComment.setEdited(false);
            documentComment.setDocumentId(documentId);
            return documentCommentRepository.save(documentComment);
        }
        else{
            DocumentComment oldComment = documentCommentRepository.findById(documentComment.getId()).orElse(new DocumentComment());

            oldComment.setComment(documentComment.getComment());
            oldComment.setCommentUser(username);
            oldComment.setEdited(true);
            oldComment.setUpdateDate(Util.getRealDate());
            oldComment.setUpdateTime(Util.getRealTime());

            return documentCommentRepository.save(oldComment);
        }
    }

    public List<DocumentComment> listComment(UUID documentId){
        return documentCommentRepository.findByDocumentId(documentId);
    }

    public ResponseDTO deleteComment(UUID id){
        DocumentComment documentComment = documentCommentRepository.findById(id).orElse(null);
        ResponseDTO response= new ResponseDTO();
        if(documentComment != null){
            documentCommentRepository.delete(documentComment);
            response.setMessage("Comment deleted successfully.");
        } else {
            response.setMessage("Comment was already deleted.");
        }
        return response;
    }
}
