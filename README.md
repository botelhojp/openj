# Framework Open Jade

* Faça um clone do projeto: ('git clone https://github.com/botelhojp/framework-open-jade.git')


## Instale o catálogo do Open Jade

O catálogo do Open Jade permite a criação de projetos Open Jade a partir de modelos chamados pelo maven de archetype.


Pre-requisitos:

* Eclipse Juno (Recomendado, mas pode-se utilizar qualquer IDE Java que aceite maven)
* Se eclipse Instale os plugins: m2eclipse para uso do maven e o egit caso queira coloborar com o projeto

Procedimentos:

* Acesse o menu: window -> preferences -> Maven -> Arquetipes

* Adicionar um catálogo remoto. Botao "Add Remote Catalog.."

* No campo Catalog File informe: http://www.ppgia.pucpr.br/~vanderson/repositories/archetype-catalog.xml

* No campo Description informe um nome: Open Jade

* Confirme e feche a janela de preferências

## Crie seu primeiro projeto

* Acesso o menu: File -> New -> Project

* Escolha o tipo de projeto "Project Maven"

* Confirme, na tela "Select an Archetype" selecione o Catálogo criado "Open Jade"

* Selecione a versão do archetype

* informe um Group ID e um Artifact ID e conclua a criação do projeto

## Rode seu projeto

* Rode a classe Boot.java (botão direiro "Run Application")

