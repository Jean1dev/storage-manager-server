package com.storage.manager.s3.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(description = "Recurso para manipulação do storage AMAZON S3", name = "S3 API")
public interface S3ApiDocs {

    @Operation(description = "retorna o nome de todos os documentos no bucket")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "objetos listados"
                    )
            }
    )
    List<String> listAllObjectsInBucket();

    @Operation(description = "Download arquivo pelo nome")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "arquivo baixado"
                    )
            }
    )
    ResponseEntity<Resource> downloadByName(String name) throws Exception;
}
