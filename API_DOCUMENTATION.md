# Storage Manager Server — Documentação das APIs

## Visão Geral

O **Storage Manager Server** é uma aplicação Spring Boot que provê uma API REST unificada para gerenciamento de arquivos em múltiplos provedores de armazenamento:

- **Local Storage** — armazenamento no sistema de arquivos local
- **Amazon S3** — armazenamento em buckets S3 da AWS
- **Google Cloud Storage** — armazenamento em buckets do GCS

**Base URL:** `http://<host>:<port>`  
**Documentação interativa (Swagger UI):** `/swagger-ui/index.html`  
**Versão da API:** v1

---

## Autenticação

A autenticação é gerenciada por variáveis de ambiente/configuração para cada provedor:

- **S3:** credenciais AWS configuradas via `application.yml` ou variáveis de ambiente
- **GCS:** credenciais Google configuradas via `application.yml` ou variáveis de ambiente

---

## 1. Local Storage

**Base path:** `/v1/local`  
**Tag:** `v1/Local Storage`

### POST `/v1/local` — Upload de arquivo

Realiza o upload de um arquivo para o armazenamento local.

**Content-Type:** `multipart/form-data`

**Parâmetros (form-data):**

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `file` | MultipartFile | Sim | Arquivo a ser enviado |

**Resposta — 200 OK:**

```json
{
  "filename": "nome-do-arquivo.ext",
  "storageLocation": "/caminho/para/o/arquivo"
}
```

---

### POST `/v1/local/perform-upload` — Upload de alta performance

Upload otimizado de arquivo para armazenamento local.

**Content-Type:** `multipart/form-data`

**Parâmetros (form-data):**

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `file` | MultipartFile | Sim | Arquivo a ser enviado |

**Resposta — 200 OK:**

```json
{
  "filename": "nome-do-arquivo.ext",
  "storageLocation": "/caminho/para/o/arquivo"
}
```

---

## 2. Amazon S3

**Base path:** `/v1/s3`  
**Tag:** `v1/Amazon S3 API`

### GET `/v1/s3` — Listar objetos de um bucket

Retorna todos os nomes de objetos armazenados em um bucket S3.

**Parâmetros (query):**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `bucketName` | string | Sim | Nome do bucket S3 |

**Resposta — 200 OK:**

```json
["arquivo1.png", "arquivo2.pdf", "pasta/arquivo3.txt"]
```

---

### GET `/v1/s3/buckets` — Listar buckets disponíveis

Retorna todos os buckets S3 disponíveis na conta configurada.

**Parâmetros:** nenhum

**Resposta — 200 OK:**

```json
["bucket-producao", "bucket-backup", "bucket-dev"]
```

---

### GET `/v1/s3/download/{name}` — Download de arquivo

Faz o download de um arquivo de um bucket S3.

**Parâmetros (path):**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `name` | string | Sim | Nome do arquivo no bucket |

**Parâmetros (query):**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `bucketName` | string | Sim | Nome do bucket S3 |

**Resposta — 200 OK:** conteúdo binário do arquivo (Resource).

---

### GET `/v1/s3/backup` — Backup de bucket

Cria um backup compactado de todos os arquivos de um bucket.

**Parâmetros (query):**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `bucketName` | string | Sim | Nome do bucket S3 |

**Resposta — 200 OK:** arquivo compactado (`byte[]`).

---

### POST `/v1/s3` — Upload de arquivo

Faz o upload de um arquivo para um bucket S3.

**Content-Type:** `multipart/form-data`

**Parâmetros (form-data):**

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `file` | MultipartFile | Sim | Arquivo a ser enviado |

**Parâmetros (query):**

| Parâmetro | Tipo | Obrigatório | Padrão | Descrição |
|-----------|------|-------------|--------|-----------|
| `bucket` | string | Não | `""` | Nome do bucket de destino |

**Resposta — 200 OK:** URL ou identificador do arquivo enviado (`string`).

---

### DELETE `/v1/s3` — Deletar arquivo

Remove um arquivo de um bucket S3.

**Parâmetros (query):**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `file` | string | Sim | Nome do arquivo a ser removido |
| `bucket` | string | Sim | Nome do bucket |

**Resposta — 200 OK:** sem corpo.

---

### Tratamento de erros — S3

Exceções do S3 são capturadas globalmente e retornam **HTTP 400**:

```json
{
  "error": "descrição do erro",
  "details": {},
  "message": "mensagem detalhada"
}
```

---

## 3. Google Cloud Storage

**Base path:** `/v1/cloud-storage`  
**Tag:** `v1/Google Cloud Storage API`

### POST `/v1/cloud-storage` — Upload de arquivo

Faz o upload de um arquivo para um bucket do Google Cloud Storage.

**Content-Type:** `multipart/form-data`

**Parâmetros (form-data):**

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `file` | MultipartFile | Sim | Arquivo a ser enviado |

**Parâmetros (query):**

| Parâmetro | Tipo | Obrigatório | Padrão | Descrição |
|-----------|------|-------------|--------|-----------|
| `bucket` | string | Não | `default_bucket` | Nome do bucket de destino |

**Resposta — 200 OK:** URL ou identificador do objeto (`string`).

---

### GET `/v1/cloud-storage` — Obter conteúdo de objeto

Retorna o conteúdo de um objeto armazenado no GCS.

**Parâmetros (query):**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `bucketName` | string | Sim | Nome do bucket |
| `objectName` | string | Sim | Nome do objeto |

**Resposta — 200 OK:** conteúdo binário do arquivo com `Content-Type` apropriado.

---

### GET `/v1/cloud-storage/{bucketName}` — Listar objetos de um bucket

Lista todos os objetos armazenados em um bucket do GCS.

**Parâmetros (path):**

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `bucketName` | string | Sim | Nome do bucket |

**Resposta — 200 OK:**

```json
["objeto1.jpg", "objeto2.csv", "pasta/objeto3.json"]
```

---

### GET `/v1/cloud-storage/buckets` — Listar buckets disponíveis

Retorna todos os buckets disponíveis no projeto GCS configurado.

**Parâmetros:** nenhum

**Resposta — 200 OK:**

```json
["bucket-imagens", "bucket-documentos", "bucket-logs"]
```

---

## 4. Endpoints de Monitoramento (Actuator)

Fornecidos pelo Spring Boot Actuator.

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `/actuator/health` | GET | Status de saúde da aplicação |
| `/actuator/info` | GET | Informações da aplicação |
| `/actuator/prometheus` | GET | Métricas no formato Prometheus |
| `/actuator/metrics` | GET | Lista de métricas disponíveis |

---

## Métricas

O serviço S3 exporta métricas via Micrometer (Prometheus) para as seguintes operações:

| Métrica | Tipo | Descrição |
|---------|------|-----------|
| contador de listagem | Counter | Total de requisições de listagem de objetos |
| contador de download | Counter | Total de downloads realizados |
| contador de upload | Counter | Total de uploads realizados |
| contador de delete | Counter | Total de deleções realizadas |
| tempo de download | Timer | Duração das operações de download |
| tempo de upload | Timer | Duração das operações de upload |

---

## Resumo dos Endpoints

| Método | Path | Provedor | Descrição |
|--------|------|----------|-----------|
| POST | `/v1/local` | Local | Upload de arquivo |
| POST | `/v1/local/perform-upload` | Local | Upload de alta performance |
| GET | `/v1/s3` | S3 | Listar objetos de bucket |
| GET | `/v1/s3/buckets` | S3 | Listar buckets disponíveis |
| GET | `/v1/s3/download/{name}` | S3 | Download de arquivo |
| GET | `/v1/s3/backup` | S3 | Backup de bucket |
| POST | `/v1/s3` | S3 | Upload de arquivo |
| DELETE | `/v1/s3` | S3 | Deletar arquivo |
| POST | `/v1/cloud-storage` | GCS | Upload de arquivo |
| GET | `/v1/cloud-storage` | GCS | Obter conteúdo de objeto |
| GET | `/v1/cloud-storage/{bucketName}` | GCS | Listar objetos de bucket |
| GET | `/v1/cloud-storage/buckets` | GCS | Listar buckets disponíveis |
