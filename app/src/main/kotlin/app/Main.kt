package app

fun main() {
    /*
    val myClass = MyClass()
    val wrappedMyClass = MyClassWrapper(myClass)
    wrappedMyClass.sayHello()
    wrappedMyClass.compute()
     */

    // Section 1 - Greeting
    val myClass = MyClass()
    val wrappedMyClass = MyClassWrapper(myClass)
    wrappedMyClass.sayHello()
    wrappedMyClass.compute()

    // Section 2 - Regex
    val input = "Name: John Address: 123 Street"
    val extractor = DataProcessorExtractor(input)
    println("Name: ${extractor.getName()}")
    println("Address: ${extractor.getAddress()}")

}