import groovy.transform.Field
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult

// 该脚本文件可以通过配置直接运行

/*
    官方文档，不会用就找文档去查，最好用的方式： http://docs.groovy-lang.org/docs/latest/html/api/

    笔记：
        1. 注释标记和 Java 一样，支持//或者/**
        2. 语句可以不用分号结尾
        3. 在Groovy中，所有的Class类型，都可以省略.class，比如
            def classFun(Class clazz) {
                 println(clazz.getCanonicalName())
            }
            // 两种写法等价
            classFun(File.class)
            classFun(File)
        4. 类的属性可以通过语法糖的方式直接通过  类.属性 的方式来访问的，更加方便
        5. 在Groovy中，当对同一个对象进行操作时，可以使用 with (类似 Kotlin 的 with 方法)
        6. 在Groovy中，判断是否为真可以更简洁(类似于 JS 的语法)，比如：
                if (list3 != null && list3.size() > 0) {}
                等价于 if(list3){}
        7. 可以使用 ?: 来简化三元表达式（类似于 Kotlin）,比如 def result = name ?: 'unKnow'
        8. 可以使用 ? 进行非空判断 ，比如 order?.customer?.address （跟 Kotlin 的用法一致）
        9. 在Groovy中，可以使用 assert 来设置断言，当断言的条件为 false 时，程序将会抛出异常，比如 assert  1+1 == 3
        10.在Groovy中，switch方法变得更加灵活，可以同时支持更多的参数类型，详情看示例
        11.在Groovy中，==相当于Java的equals，，如果需要比较两个对象是否是同一个，需要使用.is()

      变量定义：
        1. 支持  动态类型  ，即定义变量的时候可以不指定其类型
        2. 定义变量的关键字为 def ,注意：虽然 def 不是必须写的，为了代码清晰，最好加上

      函数定义：
        1. 定义函数时，入参类型是可以不指定的(类似js)，返回类型也可以没有(没有返回类型时必须加上 def 关键字)，有返回类型可以不加(类似显式指定变量类型，这时候会提示建议去掉 def 关键字)
        2. 注意：定义函数时可以不指定返回类型，不代表没有函数返回值，最后一行代码的执行结果就是本函数的返回值，具体返回什么类型，根据最后一行代码动态决定，没有具体类型就返回 null
        3. 注意：所谓无返回类型的函数，内部都是按返回 Object 类型来处理的。毕竟，Groovy 是基于 Java的，而且最终会转成 Java Code 运行在 JVM
        4. 注意：无论是有指定返回类型还是没有，都是可以不使用 return 语句的，自动根据最后一行代码决定返回类型
        5. 所谓的无指定返回类型，可以理解为动态类型，可以返回任何类型，如果指定了返回类型，则必须返回正确的指定数据类型
        6. 函数调用的时候还可以不加括号(会把属性和调用混淆)，建议Groovy API 或者 Gradle API 中比较常用的，比如 println，就可以不带括号，否则还是加上吧，更清晰一些

      字符串使用：
        1. 单引号 '' 对应 Java 中的 String ，不对 $ 符号进行转义
        2. 双引号 "" 会对 $ 符号进行转义，会执行 ${} 表达式 (类似于 kotlin 的字符串模板功能)
        3. 三引号 '''''' 支持随意换行，会保持书写时的格式

      数据类型：
        1. 基本数据类型： Groovy 中的所有事物都是对象，像 int ,boolean 在 Groovy 中对应着 Integer,Boolean 对象 (跟 Kotlin 非常像)
        2. 容器类：
                - List : 列表 ，其底层对应 Java 中的 List 接口，一般用 ArrayList 作为真正的实现类
                    定义： 用 [ el ,el ,el] 的形式定义列表
                    访问或赋值： 用 [ index ] 这种方式访问元素，  用 [ index ] = xxx 的方式设值
                    注意事项：
                        1. 没有索引越界问题，访问不存在的索引，返回的是 null 值
                        2. 如果索引超过当前列表长度，List 会自动往该索引添加元素，没被添加元素的位置默认值为 null
                        3. 不管列表之前存放的是什么值，没被填充元素的索引位置默认还是 null，之前存放的元素类型也可以被任意改动，全是动态类型
                        4. 添加元素 ,下面两种写法是等价的 list3.add('d')  或  list3 << 'd' ，左移运算符，表示添加元素
                        5. 可以链式调用 list3 << 'e' << 'd' << 'g'

                - Map : 映射 ，其底层对应 Java 中的 LinkedHashMap 实现类
                    定义： 用 [ key : value, key : value] 的方式定义 Map，key 必须为字符串，value 可为任何对象
                    访问或赋值： 用成员变量方式 .key 或用 [ key ] 的方式来访问和赋值元素
                    注意事项：
                        1. key 可以不用引号包起来，但是默认还是会被转为字符串
                        2. 通过 [ key ] 的方式来访问元素时， key 是字符串，一定要用引号包起来
                        3. 如果想引用变量，需要给 key 加上括号

                - Range : 区间 ，它其实是 List 的一种拓展
                    定义： 用 由 begin 值+两个点+end 值来定义，比如 1..5 (1到5) ， 1..<5 (1到4，不包括最后一个元素)
                    访问或赋值：
                    注意事项：
                        1. 可以直接通过 .属性 的方式来获取对应的属性，是一种语法糖，其实调用的是 getXXX 方法 (类似 Kotlin)

      闭包(Closure)：
        定义：是一种数据类型，它代表了一段可执行的代码
        用法： def xxx = {parameters -> code} 箭头前面是参数定义，箭头后面是代码，如果没用 return 返回结果，则最后一行代码决定返回结果
              如果不需要参数，直接这样写就行 def xxx = { code }，不需要 -> 符号  (感觉就是 lambda 表达式)
        调用： 通过 闭包对象.call(参数) 或者 闭包对象(参数) ，比如 closure.call("p1","p2") 或 closure("p1","p2")
        注意事项： 1. 如果闭包没定义参数的话，则隐含有一个参数，这个参数名字叫 it，it 代表闭包的参数。即无定义参数的闭包调用时，你可以传入一个参数，默认
                 通过 it 来接收
                 2. 如果完全不想接收参数，可以这样定义，def xxx = { -> code }  ，没有入参，但是加上箭头，这种方式定义的闭包，既没有入参也没有默认参 it ，真正意义的无参
                 3. 函数带闭包入参的时候，省略调用时的 ()，能让代码简洁，看起来更像脚本语言，在 Gradle 中经常会这样用，要特别注意（要看懂）
                    比如： customFun 100 ,"test",{ } ，这一连串代码其实就是一个函数调用和函数的入参，只是看起来像脚本
                 4. 调用 API 时，一定要确定好闭包的入参类型，入参个数和返回值，不然也不知道怎么写

      脚本：
        定义： 在Java的一个源码文件中，不能不写class（interface或者其他....），而 Groovy 可以像写脚本一样，把要做的事情都写在 xxx.groovy 中，而且可
              以通过 groovy xxx.groovy 直接执行这个脚本。其实 Groovy 会把内容转换成一个 Java 类，然后执行其中的 static main 方法
        注意：
            1. 脚本中的变量和作用域：函数会被定义成脚本名称生成的类的成员函数，如果外面用 def（或者指明类型）定义的变量，会在生成的 run 函数中定义，因为成员函数是访问不了令一个函数的变量的，作用域不一样
               比如：def var = 100  // 注意，这个变量用了 def ，如果是 int x = 100 这样定义也是
                    def scriptTest() {
                     println(var)    // 会报错，找不到变量 var ，因为作用域不一样，无法访问
                    }
            2. 针对1中的问题，如果想访问定义的变量，需要去除 def 或 类型，像这样： var = 100，那么 var 就会变成成员变量
            （其实是在run 函数中动态加进去的，所以其他脚本调用该脚本的成员函数时，还是不能访问），也就能被成员函数访问了
            3. 针对2中的问题，可以通过 @Field 注解，让变量真正地变成成员变量，这样就可以在 script 中定义那些需要输出给外部脚本或类使用的变量了！
               比如 ： @Field var = 100
            4. 脚本之间调用时，要先导入对应的文件，然后再通过 new 来创建对应的对象，就可以使用该对象所定义的所有方法和变量了

       文件 I/O 操作：
            1. 涉及到的相关类和文档地址

                java.io.File: http://docs.groovy-lang.org/latest/html/groovy-jdk/java/io/File.html
                java.io.InputStream: http://docs.groovy-lang.org/latest/html/groovy-jdk/java/io/InputStream.html
                java.io.OutputStream: http://docs.groovy-lang.org/latest/html/groovy-jdk/java/io/OutputStream.html
                java.io.Reader: http://docs.groovy-lang.org/latest/html/groovy-jdk/java/io/Reader.html
                java.io.Writer: http://docs.groovy-lang.org/latest/html/groovy-jdk/java/io/Writer.html
                java.nio.file.Path: http://docs.groovy-lang.org/latest/html/groovy-jdk/java/nio/file/Path.htm

            2. 读写文件 ：
                2.1 创建 File 文件，调用其相应的方法，比如 eachLine、withInputStream 等带闭包的API ，相当简洁和方便
                2.2 OutputStream 的<<操作符重载，完成从 inputStream 到 OutputStream，详细使用看案例
                注意： File 文件的路径问题，./ 代表项目的根路径，不是模块的根路径

        xml 解析：
            1. 通过 XmlSlurper 类的 parse 方法解析对应的 xml 文件
            2. 通过 e1.e2.e3 这种方式就能访问元素
            3. 通过 类似 author['@id'] 或 author.@id 的方式获取元素的属性
            4. 通过 类似 author.text() 或直接输出 author 的方式获取元素的值

 */

/*  变量定义 */

def varTest() {
    def var = 1    // 定义变量 ，结尾不用写分号
    def var2 = "变量"     // 定义变量，字符串类型
    def int var3 = 100      // 定义变量，显式指定变量类型，这时候会提示建议去掉 def 关键字
    println(var)
    println(var2)
    println(var3)
}
//varTest()

/*  函数定义 */

String testFun(arg1, arg2) {    // 无需指定入参的类型 ，指定了函数返回类型，则可不必加 def 关键字来定义函数
    "test"      // 无论是有指定返回类型还是没有，都是可以不使用 return 语句的，自动根据最后一行代码决定返回类型
}

def noReturnTypeFun() {     // 没有返回类型的函数定义，必须使用 def 关键字
    "result"        //如果这是最后一行代码，这里返回的是 String 类型
    1000            //如果这是最后一行代码，这里返回的是 Integer 类型
    println()       //如果这是最后一行代码，这里返回的是 null
}
//println(noReturnTypeFun())
//println(testFun(1,2))

def classFun(Class clazz) {
    println(clazz.getCanonicalName())
}
//classFun(File.class)
//classFun(File)

/*  字符串练习 */

String stringFun() {
    def singleQuote = 'I am $ dollar'       // 输出 I am $ dollar

    def one = 1
    def doubleQuote = "I am ${one} dollar"      // I am 1 dollar

    // 3个引号，支持随意换行，会保持书写时的格式
    def multiLines = '''begin
        line 1
        line 2
        line 3 '''
}
//println(stringFun())
//println stringFun()     // println 函数没加括号，可以正常输出
//stringFun       // 没加括号，会误认为是一个变量，会报错，因为没有声明这个变量


/*  数据类型 */

def dataTypeTest() {
    def data = 1
    println(data.getClass().getCanonicalName())     // 输入 java.lang.Integer
}
//dataTypeTest()

/*  容器类型 */

def listTest() {
    // 用 [ el ,el ,el] 的形式定义列表
    def list = [1, "test", true]
    def list2 = [1, 2, 3]
    def list3 = ["a", "b", "c"]

    // 全部输出 java.util.ArrayList ，泛型会被擦除
//    println(list.getClass().getCanonicalName())
//    println(list2.getClass().getCanonicalName())
//    println(list3.getClass().getCanonicalName())

    // 元素访问
    println(list[1])        // test
    println(list[100])      // null
    println(list.size())    // 3

    // 元素设值 ，如果索引超过当前列表长度，List 会自动往该索引添加元素，没被添加元素的位置默认值为 null
    list[0] = 50
    list[4] = "第5个元素内容"
    println(list)       // [50, test, true, null, 第5个元素内容]

    // 注意，虽然这个列表全是 Integer 类型 ，没被填充元素的索引位置默认还是 null
    list2[5] = 5
    list2[1] = "test"   // 把第二个元素设为字符串类型也是可以的，全是动态类型
    println(list2)      // [1, 2, 3, null, null, 5]

    // 添加元素 ,下面两种写法是等价的
    list3.add('d')
    list3 << 'd'
    // 链式调用
    list3 << 'e' << 'd' << 'g'
    // 通过 with 操作符进行操作
    list3.with {
        add('h')
    }
    println(list3)

    list3.clear()
    // 以下两种判断方式等价
    if (list3 != null && list3.size() > 0) {
        println("条件成立")
    } else {
        println("条件不成立")
    }

    if (list3) {
        println("条件成立")
    } else {
        println("条件不成立")
    }
}
//listTest()

def mapTest() {
    // 用 [ key : value, key : value] 的方式定义 Map，key 必须为字符串，value 可为任何对象
    def map = ["key1": 1, "key2": true, "key3": "test"]
    // 用单引号的方式，也就是单双引号的功能区别，本质都是字符串
    def map2 = ['key1': 1, 'key2': true, 'key3': "test"]
    // key 可以不用引号包起来，但是默认还是会被转为字符串
    def map3 = [key: 5, key2: "字符串类型"]

    // 注意！！ 如果不用引号，要注意变量引用和字符串的使用区别，不要混淆
    def key1 = "我是变量key1"
    // 这种方式，key 没有加括号，会被默认转为字符串，不会引用变量，所以输出为 [key1:值可为任何类型]
    def map4 = [key1: "值可为任何类型"]
    // 如果想引用变量，需要给 key 加上括号，会输出 [我是变量key1:值可为任何类型]
    def map5 = [(key1): "值可为任何类型"]
    println(map4)
    println(map5)

    // 元素访问
    // 方式一，成员变量方式
    println(map.key1)
    // 方式二，通过 [key] 的方式 ，注意这里的 key 是字符串，一定要用引号包起来
    println(map["key1"])

    // 元素赋值
    map.key1 = 100
    map['key2'] = false

    println(map)    // 输出 [key1:100, key2:false, key3:test]

    // 遍历
    map.each { key, value ->
        println("$key -> $value")
    }
}
//mapTest()

def rangeTest() {
    // 用 由 begin 值+两个点+end 值来定义
    def range = 1..5
    // 如果不想包含最后一个元素，可以这样写
    def range2 = 1..<5
    println(range2.from)    // 起始值 ，语法糖，其实调用的是 getFrom 方法
    println(range2.to)      // 结束值 ，语法糖，其实调用的是 getTo 方法
}
//rangeTest()

def closureTest() {
    // 箭头前面是参数定义，箭头后面是代码，如果没用 return 返回结果，则最后一行代码决定返回结果
    def closure = { String p1, String p2 ->
        "this is Code"
        "result"
    }

    // 无参的 Closure ，其实有默认参数 it
    def closure2 = {
        "result"
        it      // 调用时没传参，it 为 null ，否则为传入的参数值
    }

    // 这种方式定义的闭包，既没有入参也没有默认参 it ，真正意义的无参
    def closure3 = { ->
        "result"
    }

    // 调用
    def result = closure.call("p1", "p2")
    def result2 = closure("p1", "p2")
    println(result)
    println(result2)

    println(closure2())     // 输出 null
    println(closure2(100))      // 输出 100


    def list = [1, 2, 3, 4]
    list.each {

    }

}

def customFun(int p1, String p2, Closure closure) {
    // do somethings
    closure()   // 调用闭包
}

def customFun2(int p1, String p2) {
    // do somethings
}

//closureTest()

// 函数调用 ，可以不加括号
customFun 100, "test", {

}

// 函数调用 ，可以不加括号
customFun2 100, "test"

/* 脚本类 */

// 这样声明，用了 def ，不能被外面定义的函数访问
def var = 100
// 这样声明，指定了类型，不能被外面定义的函数访问
int var2 = 50
// 这样声明可以，会变成成员变量，可以被外面定义的函数访问，但是不能被其他脚本访问
var3 = "没有def和类型"
// 这样声明就完全变成成员变量，可以在本脚本访问，也可以让外部脚本访问
@Field var4 = "没有def和类型，加上 @Field 注解"

def scriptTest() {
//    println(var)    // 会报错，找不到变量 var ，因为作用域不一样，无法访问
//    println(var2)    // 会报错，找不到变量 var2 ，因为作用域不一样，无法访问
//    println(var3)
    println(var4)
}
//scriptTest()

/* 文件 I/0 操作 */

def readFileTest() {
//    def targetFile = new File('G:/fanda_all_projects/gradle_projects/groovy_projects/GroovyPractice/groovy/src/main/groovy/content.txt' )
    def targetFile = new File('./groovy/src/main/groovy/content.txt')
    // 读该文件中的每一行：eachLine 的唯一参数是一个 Closure。Closure 的参数是文件每一行的内容
//    targetFile.eachLine {
//        println(it)
//    }

    // 文件内容一次性读出，返回类型为 byte[]
    println(targetFile.bytes)

    def ism = targetFile.newInputStream()
    // 操作 ism，最后记得关掉
    ism.close()

    // 常用方式，通过闭包来操作
    targetFile.withInputStream {
        // 操作 ism ，不用主动 close，Groovy 会自动替你 close
    }
}
//readFileTest()

def writeFileTest() {
    // 这里写一个 copy 文件的例子
    // 源文件如下：
    def srcFile = new File("./groovy/src/main/groovy/content.txt")
    // 复制到目标文件中去
    def targetFile = new File("./groovy/src/main/groovy/copy.txt")
    if (!targetFile.exists()) targetFile.createNewFile()
    targetFile.withOutputStream { os ->
        srcFile.withInputStream { is ->
            // 利用 OutputStream 的<<操作符重载，完成从 inputStream 到 OutputStream
            os << is
        }
    }
}
//writeFileTest()

/* 解析 xml */

def xmlTest() {
    // 第一步 ，创建 XmlSlurper 类
    def xmlSlurper = new XmlSlurper()
    def targetFile = new File('./groovy/src/main/groovy/test.xml')
    println(targetFile.absolutePath)
    def result = xmlSlurper.parse(targetFile)
    println result.toString()
    // 要访问 id=4 的 book 元素，result 代表根元素 response (在当前 xml 文件)，通过 e1.e2.e3 这种方式就能访问元素
    def book4 = result.value.books.book[3]
    // 直接访问，默认输出的就是元素的值
    println(book4.author)
    // 获取元素的值
    println(book4.author.text())
    // 获取元素的属性，比如这里的 id 属性
    println(book4.author['@id'])
    // 方式二
    println(book4.author.@id)
}
xmlTest()

/* 断言 */

def assertTest() {
    assert 1 + 1 == 3
}
//assertTest()

/* switch 更加强大*/
def switchTest() {
    def x = 1.23
    def result = ''
    switch (x) {
        case 'foo':
            result = 'found foo'
            // 没加上 break ，会穿透
        case 'bar':
            result += 'bar'
        case [4, 5, 6, 'inList']:
            result = 'list'
            break
        case 12..30:
            result = 'range'
            break
        case Integer:
            result ='integer'
            break
        case Number:
            result = 'number'
            break
        case {it >3}:
            result ='number >3'
            break
        default:
            result = 'default'
    }
    println(result)
}
//switchTest()

class Test implements Cloneable {}
// clone()的正确调用是需要实现Cloneable接口，如果没有实现Cloneable接口，并且子类直接调用Object类的clone()方法，则会抛出CloneNotSupportedException异常。
def equalsAndIsTest() {
    Test a = new Test()
    Test b = a.clone()
//    assert a == b
    assert !a.is(b)
}
equalsAndIsTest()