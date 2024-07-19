package com.storage.manager.gcstorage.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(description = "Recurso para manipulação do storage GOOGLE CLOUD STORAGE", name = "v1/Google Cloud Storage API")
public interface GCStorageApiDocs {

    @Operation(description = "faz o upload de um arquivo para o bucket")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "arquivo enviado"
                    )
            }
    )
    ResponseEntity<String> uploadFile(MultipartFile multipartFile, String __bucket);

    @Operation(description = "recupera um item do bucket")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "item"
                    )
            }
    )
    ResponseEntity<byte[]> getContent(String bucketName, String objectName);

    @Operation(description = "lista os itens do bucket")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "listados"
                    )
            }
    )
    List<String> list(String bucketName);

    @Operation(description = "lista os buckets disponiveis")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "listados"
                    )
            }
    )
    List<String> listBuckets();
}
