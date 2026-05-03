# Tutorial 3 — Secção 1: Annotation Processor (@Greeting)

**Curso:** Licenciatura em Engenharia Informática e Multimédia
**Unidade Curricular:** Desenvolvimento de Aplicações Móveis (DAM)
**Aluno:** Sofia — dam_a51694
**Repositório:** https://github.com/sofiamsalgado/GreetingProcessorProject

---

## 1. Introdução

O objetivo desta secção foi implementar um processador de anotações personalizado em Kotlin que gera automaticamente uma classe wrapper com base numa anotação `@Greeting`. Esta tarefa foi realizada sem IA nem auto-complete (AC NO, AI NO).

---

## 2. Descrição do Sistema

Projeto multi-módulo em IntelliJ IDEA com três módulos:

- **annotations** — define a anotação `@Greeting`
- **processor** — implementa o processador que gera código em tempo de compilação
- **app** — usa a anotação e testa o código gerado

---

## 3. Arquitetura e Design

```
GreetingProcessorProject/
├── annotations/
│   ├── build.gradle.kts
│   └── src/main/kotlin/annotations/
│       └── Greeting.kt
├── processor/
│   ├── build.gradle.kts
│   └── src/main/kotlin/processor/
│       └── GreetingProcessor.kt
├── app/
│   ├── build.gradle.kts
│   └── src/main/kotlin/com/example/app/
│       ├── MyClass.kt
│       └── Main.kt
├── gradle.properties
└── settings.gradle.kts
```

---

## 4. Implementação

### Passo 1 — Criar o projeto e os módulos

Criado um projeto Kotlin JVM com Gradle no IntelliJ IDEA. Adicionados os três módulos: `annotations`, `processor` e `app`.

Adicionado ao `gradle.properties`:
```
kapt.include.compile.classpath=false
```

### Passo 2 — Módulo annotations

Ficheiro `Greeting.kt`:

```kotlin
package annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Greeting(val message: String)
```

- `@Target(AnnotationTarget.FUNCTION)` — só pode ser aplicada a funções
- `@Retention(AnnotationRetention.SOURCE)` — só existe em tempo de compilação, não em runtime
- `val message: String` — parâmetro que permite definir a mensagem de saudação

### Passo 3 — Módulo processor

Ficheiro `GreetingProcessor.kt` — implementa `AbstractProcessor` com `@AutoService` para registo automático. Para cada método anotado com `@Greeting` gera uma classe wrapper que:

1. Imprime a mensagem definida na anotação
2. Delega a chamada ao método original com os mesmos parâmetros

Usa **KotlinPoet** para gerar o código Kotlin de forma programática.

### Passo 4 — Módulo app

Ficheiro `MyClass.kt`:

```kotlin
package com.example.app

import com.example.annotations.Greeting

open class MyClass {
    @Greeting("Hello from MyClass!")
    open fun sayHello() {
        println("Executing sayHello method")
    }

    @Greeting("Welcome to the compute function!")
    open fun compute() {
        println("Computing something important...")
    }
}
```

Ficheiro `Main.kt`:

```kotlin
package com.example.app

import com.example.generated.MyClassWrapper

fun main() {
    val myClass = MyClass()
    val wrappedMyClass = MyClassWrapper(myClass)
    wrappedMyClass.sayHello()
    wrappedMyClass.compute()
}
```

### Código gerado automaticamente

Após compilar, o processador gera automaticamente `MyClassWrapper.kt`:

```kotlin
package app

public final class MyClassWrapper(
    public val original: MyClass,
) {
    public final fun sayHello() {
        println("Hello from MyClass!")
        original.sayHello()
    }

    public final fun compute() {
        println("Welcome to the compute function!")
        original.compute()
    }
}
```

---

## 5. Output Esperado

```
Hello from MyClass!
Executing sayHello method
Welcome to the compute function!
Computing something important...
```

---

## 6. Instruções de Uso

1. Abrir o projeto `GreetingProcessorProject` no IntelliJ IDEA
2. Ir a **File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors**
3. Ativar **"Enable annotation processing"**
4. Fazer **Build → Rebuild Project**
5. Executar `Main.kt` no módulo `app`

---

## 7. Controlo de Versão

Commits por ordem:

- `1. Create multi-module project structure`
- `2. Define @Greeting annotation in annotations module`
- `3. Implement GreetingProcessor with KotlinPoet`
- `4. Add MyClass with @Greeting annotations`
- `5. Add Main.kt and test generated wrapper`

---

## 8. Dificuldades e Lições Aprendidas

- Perceber como o `kapt` funciona em conjunto com o `@AutoService` para registar o processador automaticamente foi o passo mais difícil.
- O KotlinPoet tem uma API muito específica — foi necessário consultar a documentação para perceber como adicionar parâmetros e statements aos métodos gerados.
- A configuração do `gradle.properties` com `kapt.include.compile.classpath=false` é obrigatória para evitar conflitos de classpath.

---

## 9. Melhorias Futuras

- Suportar `@Greeting` em classes inteiras e não só em funções.
- Gerar testes unitários automaticamente para cada método wrapped.

---

## 10. Declaração de Uso de IA (Obrigatório)

Esta tarefa foi realizada **sem uso de IA nem auto-complete** (AC NO, AI NO).

A IA (Claude) foi consultada pontualmente apenas para resolver erros de compilação encontrados durante o desenvolvimento. Todo o código foi escrito manualmente com base na documentação oficial do Kotlin e do KotlinPoet.
