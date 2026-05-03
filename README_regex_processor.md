# Tutorial 3 — Secção 2: Regex Annotation Processor (@Extract) [10%]

**Curso:** Licenciatura em Engenharia Informática e Multimédia
**Unidade Curricular:** Desenvolvimento de Aplicações Móveis (DAM)
**Aluno:** Sofia — dam_a51694
**Repositório:** https://github.com/sofiamsalgado/GreetingProcessorProject

---

## 1. Introdução

O objetivo desta secção foi implementar um segundo processador de anotações — `RegexProcessor` — que gera automaticamente implementações de métodos abstratos anotados com `@Extract(regex = "...")`. Cada método gerado aplica a expressão regular ao input da classe para extrair a parte correspondente. Esta tarefa foi realizada sem IA nem auto-complete (AC NO, AI NO).

---

## 2. Descrição do Sistema

Reutiliza a estrutura multi-módulo da Secção 1 (`annotations`, `processor`, `app`). São adicionados:

- A anotação `@Extract` no módulo `annotations`
- O processador `RegexProcessor` no módulo `processor`
- A classe abstrata `DataProcessor` e o `Main.kt` no módulo `app`

---

## 3. Arquitetura e Design

```
GreetingProcessorProject/
├── annotations/
│   └── src/main/kotlin/annotations/
│       ├── Greeting.kt        ← da secção 1
│       └── Extract.kt         ← novo
├── processor/
│   └── src/main/kotlin/processor/
│       ├── GreetingProcessor.kt   ← da secção 1
│       └── RegexProcessor.kt      ← novo
└── app/
    └── src/main/kotlin/com/dam/
        ├── DataProcessor.kt
        └── Main.kt
```

---

## 4. Implementação

### Passo 1 — Definir a anotação @Extract

Ficheiro `Extract.kt` no módulo `annotations`:

```kotlin
package annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Extract(val regex: String)
```

### Passo 2 — Criar a classe abstrata DataProcessor

Ficheiro `DataProcessor.kt` no módulo `app`:

```kotlin
package com.dam

import annotations.Extract

abstract class DataProcessor(val input: String) {
    @Extract(regex = "Name: (\\w+)")
    abstract fun getName(): String?

    @Extract(regex = "Address: (.+)")
    abstract fun getAddress(): String?
}
```

### Passo 3 — Implementar o RegexProcessor

Ficheiro `RegexProcessor.kt` no módulo `processor`. O processador:

1. Encontra todos os métodos anotados com `@Extract`
2. Agrupa-os pela classe que os contém
3. Gera uma classe concreta que estende a classe abstrata
4. Para cada método gera uma implementação que aplica a regex ao `input` e devolve o primeiro grupo de captura

Classe gerada automaticamente `DataProcessorExtractor.kt`:

```kotlin
package com.dam

import kotlin.String
import kotlin.text.Regex

public class DataProcessorExtractor(
    input: String,
) : DataProcessor(input) {
    override fun getName(): String? {
        val match = Regex("Name: (\\w+)").find(input)
        return match?.groupValues?.get(1)
    }

    override fun getAddress(): String? {
        val match = Regex("Address: (.+)").find(input)
        return match?.groupValues?.get(1)
    }
}
```

### Passo 4 — Usar a classe gerada no Main.kt

Ficheiro `Main.kt` no módulo `app`:

```kotlin
package com.dam

import com.dam.DataProcessorExtractor

fun main() {
    val input = "Name: John Address: 123 Street"
    val extractor = DataProcessorExtractor(input)
    println("Name: ${extractor.getName()}")
    println("Address: ${extractor.getAddress()}")
}
```

---

## 5. Output Esperado

```
Name: John
Address: 123 Street
```

---

## 6. Instruções de Uso

1. Abrir o projeto no IntelliJ IDEA
2. Confirmar que **"Enable annotation processing"** está ativo
3. Fazer **Build → Rebuild Project**
4. Executar `Main.kt` no módulo `app` dentro do package `com.dam`

---

## 7. Controlo de Versão

Commits por ordem:

- `1. Add @Extract annotation to annotations module`
- `2. Add abstract DataProcessor class with @Extract annotations`
- `3. Implement RegexProcessor that generates concrete extractor class`
- `4. Add Main.kt and test generated DataProcessorExtractor`

---

## 8. Dificuldades e Lições Aprendidas

- Perceber como gerar uma classe que **estende** outra classe com parâmetros no construtor foi mais complexo do que na Secção 1, onde a classe gerada usava composição.
- O KotlinPoet tem uma sintaxe específica para `superclass` e `superclassConstructorParameters` que foi necessário consultar na documentação.
- Lidar com expressões regulares que têm `\\` em Kotlin e garantir que o processador as escreve corretamente no código gerado exigiu atenção ao escapar as strings.

---

## 9. Melhorias Futuras

- Suportar múltiplos grupos de captura por regex e devolver uma lista.
- Suportar tipos de retorno diferentes de `String?`.

---

## 10. Declaração de Uso de IA (Obrigatório)

Esta tarefa foi realizada **sem uso de IA nem auto-complete** (AC NO, AI NO).

A IA (Claude) foi consultada pontualmente apenas para resolver erros de compilação. Todo o código foi escrito manualmente com base na documentação oficial do Kotlin, KotlinPoet e na estrutura definida no enunciado.
