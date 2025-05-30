# Sistema de Reservas de Quadras Esportivas  

Este Repositório é uma aplicação distribuída no paradigma **cliente/servidor** usando **RPC (Remote
Procedure Call)** para um **Sistema de Reservas de Quadras Esportivas**.

## Pré-requisitos

* **Java 15** ou superior
* **Maven** (para compilação)

## Como Usar

Para sua conveniência, os arquivos JAR compilados já estão disponíveis.

### JARs Pré-compilados

* [JAR do Cliente](depois-colar-aqui-o-link)
* [JAR do Servidor](depois-colar-aqui-o-link)

### Iniciando o Servidor

Para iniciar o servidor, navegue até o diretório onde está `server-jar-with-dependencies.jar` e execute o comando dentro da pasta Jars **(C:\..\src\jars)**:

```bash
java -jar server-jar-with-dependencies.jar
```

### Executando o Cliente

Para rodar o cliente, vá para o diretório onde `client-jar-with-dependencies.jar` e execute o comando dentro da pasta Jars **(C:\..\src\jars)**:

```bash
java -jar client-jar-with-dependencies.jar localhost
```

**Observação:** `localhost` é o IP ou hostname do servidor. Se nenhum valor for fornecido, o cliente assumirá `localhost` como padrão.

## Compilação

Se você preferir compilar o projeto a partir do código-fonte, certifique-se de ter o Maven instalado.

1.  Navegue até a pasta raiz do projeto.
2.  Execute o comando Maven:

    ```bash
    mvn clean package
    ```