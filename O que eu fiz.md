### ✅ **Resumo da Implementação – Sistema de Gestão de Frequência (Parte do Coordenador)**

**1. `Coordenador.java`**  
Implementei a classe `Coordenador` como uma extensão de `Usuario` e implementação da interface `MenuCoordenador`.  
Ela é responsável por **gerenciar professores e disciplinas**, com foco em integração futura com interface gráfica (Swing).

Principais funcionalidades:
- **Adicionar, remover e listar professores**
- **Atribuir e remover disciplinas de professores**
- **Gerar relatórios de professores em texto e HTML**
- **Fornecer modelos de tabelas (`DefaultTableModel`) para uso direto no Swing**
- Uso de boas práticas com `Optional`, `stream()`, encapsulamento de listas e serialização (`Serializable`)
---

**2. `MenuCoordenador.java`**  
Criei a interface `MenuCoordenador` para **padronizar as ações que o coordenador pode realizar**, tanto no console quanto com componentes gráficos Swing.

Ela define:
- Os métodos obrigatórios de gestão (`add`, `remove`, `atribuir`, `listar`, etc.)
- Métodos auxiliares para Swing:
    - `getModeloTabelaProfessores()` e `getModeloTabelaDisciplinas()`
    - `exibirMenuCoordenador(JFrame parent)` com abas para professores e disciplinas
    - `criarPainelProfessores()` e `criarPainelDisciplinas()` com estrutura básica para visualização

---

### 🔧 **Objetivo**

Essa separação permite **facilidade de manutenção, reaproveitamento de código e integração com a interface gráfica Swing**, como exigido pelo projeto e pela orientação do professor.
