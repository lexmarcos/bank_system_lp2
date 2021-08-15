# Sistema bancário LP2

## Classes:

### Bank
##### Descrição:
---
Classe que irá deter o nome do banco, código do banco (id: string) e o array decontas que este banco tem.

##### Atributos:
---
###### - private name: string:
  Indica o nome do banco, serve para ser exibido na interface

###### - private id: string:
  Indica a id do banco, será muito utilizado nas funções de buscas

###### - private costumers: arrayList<costumer>:
  Atributo para guardar os objetos de clientes que o banco tem.

##### Métodos:
  ---
###### + public addCostumer(): void:
  Método para adicionar um novo cliente ao banco.
###### + public removeCostumer(costumer): boolean:
  Método que exclui um cliente do banco, se achar o cliente ele exclui e retorna `true`, caso contrário `false`
###### + public getAccount(number): account:
  Busca e retorna uma account
###### + public getAccountByPix(pixKey): account:
  Busca e retorna uma account

### Response
##### Descrição:
---
Classe responsável por tratar saídas de métodos, geralmente de métodos que precisam de duas saídas como validação e mensagem do que ocorreu.

##### Atributos:
---
###### - private message: string:
  Atributo responsável por salvar a mensagem da ação
###### - private sucess: boolean:
  Atributo responsável por salvar se a ação foi um sucesso ou não (`true`, `false`).
  
##### Métodos:
---
###### + public getMessage(amount: float): string:
  Método responsável por retornar a mensagem da ação.
  
###### + public getSucess(amount: float): boolean:
  Método responsável por retornar o sucesso da ação.

### Account
##### Descrição:
---
Serve para alocar informações da conta do **user** e tem os métodos responsáveis pela movimentação do dinheiro
  
##### Atributos:
---
###### - private number: string:
  Número da conta, utilizado para reconhecer a conta caso o usuário ainda não possua pix.
###### - private balance: float:
  Atributo responsável por guardar a quantidade de dinheiro que o usuário possui na conta atualmente.
###### - private pixKey: string:
  Atributo responsável por salvar a key pix do usuário na conta.

##### Métodos:
---
###### + public depostit(amount: float): void:
  Método responsável por fazer depósito na própria conta do usuário.
  
###### + public transfer(amount: float, pixKey): Response:
  Método responsável por fazer a transferência de uma conta para outra atráves do pix. Caso encontre o pix e tenha saldo suficiente, faz a transferência e retorna `true` com a mensagem de sucesso, caso contrário retorna `false` com a mensagem de erro.
  
###### + public withdraw(amount: float): boolean:
  Método responsável por retirar o dinheiro da conta, fazer um saque. Se tiver dinheiro suficiente faz a retirada e retorna `true`, caso contrário retorna `false`

###### + public collect(amount: float): string:
  Método responsável por fazer uma cobrança, esse método recebe um valor para ser cobrado e retorna uma string com o id da cobrança, esse id deve ser compartilhado com a pessoa que será cobrada.
  
###### + public PayCollect(idCollect: string): Response:
  Método responsável por procurar a cobrança e efetuar o pagamento. Deve ser verificado se na conta atual existe saldo suficiente para o pagamento e se a cobrança existe, caso atenda a esses requisitos, retorna `true` e a mensagem "pagamento efetuado com sucesso", caso contrário retorna `false` e a mensagem com o erro que aconteceu.

