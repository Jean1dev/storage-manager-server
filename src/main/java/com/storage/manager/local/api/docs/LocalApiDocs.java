package com.storage.manager.local.api.docs;

import com.storage.manager.local.api.dto.UploadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@Tag(description = "Recurso para manipulação do local storage", name = "Local Storage")
public interface LocalApiDocs {
    @Operation(description = "Upload de qualquer arquivo")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "upload concluido"
                    )
            }
    )
    UploadResponseDto uploadFile(MultipartFile multipartFile);


    @Operation(description = "Upload performatico de qualquer arquivo")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "upload concluido"
                    )
            }
    )
    UploadResponseDto performUpload(MultipartFile multipartFile);
}
