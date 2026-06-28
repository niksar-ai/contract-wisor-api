package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.DocumentSortedDTO;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.model.DocumentFavourite;
import ai.niksar.contract_wisor_api.model.UserModel;
import ai.niksar.contract_wisor_api.repository.DocumentFavoriteRepository;
import ai.niksar.contract_wisor_api.util.Util;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentFavouriteService {
    @Autowired
    private DocumentFavoriteRepository documentFavoriteRepository;

    public ResponseDTO saveDocumentFav(UUID documentId){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        DocumentFavourite documentFavourite=new DocumentFavourite();
        documentFavourite.setDocumentId(documentId);
        documentFavourite.setCreateDate(Util.getRealDate());
        documentFavourite.setCreateTime(Util.getRealTime());
        documentFavourite.setCreateUser(username);
        documentFavoriteRepository.save(documentFavourite);
        ResponseDTO response= new ResponseDTO();
        response.setMessage("Document added to the favourites list successfully.");
        return response;
    }

    public List<UserModel> listUsersByDocId(UUID documentId){
        return documentFavoriteRepository.userList(documentId);
    }

    public List<DocumentSortedDTO> listFavDocumentsByUsername(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return documentFavoriteRepository.favDocumentList(username);
    }

    public ResponseDTO deleteFav(UUID id){
        String              username             = SecurityContextHolder.getContext().getAuthentication().getName();
        DocumentFavourite   documentFavourite    = documentFavoriteRepository.find(username,id);
        ResponseDTO response= new ResponseDTO();
        if(documentFavourite != null) {
            documentFavoriteRepository.delete(documentFavourite);
            response.setMessage("Document removed from the favourites list.");
        } else {
            response.setMessage("Document is not in the favourites list.");
        }
        return response;
    }

    public boolean isFavDocument(UUID documentId){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        DocumentFavourite documentFavourite= documentFavoriteRepository.find(username,documentId);
        return documentFavourite != null;
    }
}
