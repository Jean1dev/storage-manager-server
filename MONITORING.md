# Configuração de Monitoramento - Storage Manager

Este projeto inclui configuração completa para observabilidade usando Prometheus e Grafana.

## Componentes Incluídos

- **Prometheus**: Coleta e armazena métricas
- **Grafana**: Visualização de métricas e dashboards
- **Node Exporter**: Métricas do sistema operacional
- **Spring Boot Actuator**: Métricas da aplicação

## Estrutura de Arquivos

```
monitoring/
├── prometheus/
│   ├── prometheus.yml          # Configuração do Prometheus
│   └── rules/
│       └── storage-manager.yml # Regras de alerta
└── grafana/
    ├── provisioning/
    │   ├── datasources/
    │   │   └── prometheus.yml  # Configuração da fonte de dados
    │   └── dashboards/
    │       └── dashboard.yml   # Configuração dos dashboards
    └── dashboards/
        ├── storage-manager-overview.json # Dashboard da aplicação
        └── system-metrics.json           # Dashboard do sistema
```

## Como Usar

### 1. Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```bash
# AWS Configuration
AWS_KEY=your_aws_access_key_here
AWS_SECRET=your_aws_secret_key_here

# Google Cloud Configuration
GOOGLE_CLOUD_CREDENTIALS=your_google_cloud_credentials_json_here
GOOGLE_CLOUD_PROJECT=your_google_cloud_project_id_here

# Application Port
PORT=8080
```

### 2. Subir os Serviços

```bash
# Subir todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar os serviços
docker-compose down
```

### 3. Acessar as Interfaces

- **Aplicação**: http://localhost:8080
  - Health Check: http://localhost:8080/actuator/health
  - Métricas: http://localhost:8080/actuator/prometheus
- **Grafana**: http://localhost:3000
  - Usuário: admin
  - Senha: admin123
- **Prometheus**: http://localhost:9090
- **Node Exporter**: http://localhost:9100/metrics

## Dashboards Disponíveis

### Storage Manager - Overview
- Status da aplicação
- Taxa de requisições
- Taxa de erro
- Tempo de resposta
- Uso de memória JVM
- Métricas de Garbage Collection

### System Metrics
- Uso de CPU
- Uso de memória
- Uso de disco
- I/O de rede
- Load average

## Alertas Configurados

- **HighMemoryUsage**: Uso de memória acima de 80%
- **HighCPUUsage**: Uso de CPU acima de 80%
- **ApplicationDown**: Aplicação fora do ar
- **HighErrorRate**: Taxa de erro acima de 10%
- **SlowResponseTime**: Tempo de resposta acima de 2 segundos (95th percentile)

## Métricas Personalizadas

Para adicionar métricas personalizadas na aplicação:

```java
@Autowired
private MeterRegistry meterRegistry;

// Contador
Counter.builder("custom.operation.count")
    .register(meterRegistry)
    .increment();

// Timer
Timer.Sample sample = Timer.start(meterRegistry);
// ... operação ...
sample.stop(Timer.builder("custom.operation.duration")
    .register(meterRegistry));
```

## Troubleshooting

### Verificar se as métricas estão sendo coletadas
```bash
curl http://localhost:8080/actuator/prometheus
```

### Verificar configuração do Prometheus
```bash
curl http://localhost:9090/api/v1/targets
```

### Reiniciar serviços específicos
```bash
docker-compose restart prometheus
docker-compose restart grafana
```
