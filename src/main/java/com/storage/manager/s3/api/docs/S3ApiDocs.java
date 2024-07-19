package com.storage.manager.s3.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(description = "Recurso para manipulação do storage AMAZON S3", name = "v1/Amazon S3 API")
public interface S3ApiDocs {

    @Operation(description = "retorna o nome de todos os documentos no bucket")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "objetos listados"
                    )
            }
    )
    List<String> listAllObjectsInBucket(String bucketName);

    @Operation(description = "Download arquivo pelo nome")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "arquivo baixado"
                    )
            }
    )
    ResponseEntity<Resource> downloadByName(String name, String bucketName) throws Exception;

    @Operation(description = "Backup de todos os arquivos do bucket")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "arquivo baixado"
                    )
            }
    )
    byte[] backupFiles(String bucketName);

    @Operation(description = "Lista os buckets disponiveis")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "listados"
                    )
            }
    )
    List<String> availableBuckets();

    @Operation(description = "Upload arquivo para o bucket")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "upload concluido"
                    )
            }
    )
    String uploadFile(MultipartFile multipartFile, String __bucket);
}
