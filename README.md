# DesignPattern_Study_Proxy

# Notion Link
https://five-cosmos-fb9.notion.site/Proxy-313daa158fbf43a99036ab0e24dd1f07


# 프록시 (Proxy)

### 의도

다른 객체에 대한 접근을 제어하기 위해 대리자 또는 자리채움자 역할을 하는 객체

### 종류

1. 원격 프록시
    1. 서로 다른 주소 공간에 존재하는 객체를 가리키는 대표 객체로, 로컬환경에 위치
2. 가상 프록시
    1. 요청이 있을 때만 필요한 고비용 객체를 생성할 때 사용
3. 보호 프록시
    1. 원래 객체에 대한 실제 접근을 제어, 객체별로 접근 제어 권한이 다를 때 유용하게 사용
4. 스마트 포인터
    1. 실제 객체 접근이 일어날 때 추가적인 행동을 담당하는 객체
    2. 객체에 대한 참조 조사, 객체에 대한 Lock 등
    

### 구조

![image](https://user-images.githubusercontent.com/18654358/157136810-54e311bd-ecab-4bd1-81a0-527282eed702.png)

### 프록시를 알아보기 전에 클라이언트와 서버?

![image](https://user-images.githubusercontent.com/18654358/157136824-c3a857e2-99cf-42a6-820d-61713728fa39.png)

- 클라이언트(Client)와 서버(Server)라고 하면 개발자는 보통 서버 컴퓨터를 생각하게 된다.
- 그러나 사실 클라이언트와 서버(CS) 개념은 상당히 넓게 사용된다.
- 클라이언트는 의뢰인(요청자)라는 뜻이고
- 서버는 서비스나 상품을 제공하는 사람이나 물건을 뜻한다.
- 따라서 클라이언트와 서버의 기본 개념을 정리한다면 **“클라이언트는 서버에 필요한 것을 요청하고, 서버는 클라이언트의 요청을 처리”**하는 것이다.
    - 이 개념을 컴퓨터 네트워크에 도입하면 우리가 익숙한 개념이 된다.
    - *클라이언트는 웹 브라우져가 되는 것이고, 요청을 처리하는 서버는 웹 서버가 된다.*
    

### 직접 호출과 간접 호출

![image](https://user-images.githubusercontent.com/18654358/157136850-85c9ce9e-852a-49c4-bfd5-05b6b21e5bc2.png)

- 클라이언트와 서버 개념에서 일반적으로 클라이언트가 서버를 직접 호출하고
- 결과를 직접 받는다.
- **직접 호출**

![image](https://user-images.githubusercontent.com/18654358/157136875-96a6f5bd-4934-4d0d-8a37-e87f65cbf006.png)

- 클라이언트가 요청한 결과를 서버에 직접 요청하는 것이 아니라
- 어떤 대리자를 통해 간접적으로 서버에 요청할 수 있다.
    - 예를 들면, 내가 직접 마트에서 장을 볼 수 있지만, 누군가에게 대신 장을 봐달라고 부탁할 수 있다.
- 이런 개념에서 대리자를 영어로 **프록시(Proxy)**라 한다.

### 조금더 상세한 프록시의 개념정리

<aside>
💡 [https://five-cosmos-fb9.notion.site/6bb64c61de294cf092cdc631f2145425](https://www.notion.so/6bb64c61de294cf092cdc631f2145425)

</aside>

### 포인트

- 프록시는 **접근 제어**와 **부가기능을 추가**해줄 수 있는 **클라이언트 요청에 대한 대리자 역할**을 수행한다.

---

### 활용

- 우리는 MSA 구조로 프로젝트를 구성하기 위해 진행하고 있다.
- 여기서 핵심 포인트 중 하나는 MSA 구조임으로 MS간의 통신이 있다.
- MS간의 통신은 단순히 각 서비스간 API 의 Request / Response 이다.
- 여기서 MSA 구조에서는 특정 서비스간의 API 통신 구간에서 장애가 발생되면 장애가 전파될 수 있는 위험이 있다.
    - **CircuktBreaker Pattern 적용**
    - 과거 : Netflix OSS Hystrix
    - 현재 : Spring Cloud Resilience4J

**여기서 CircuktBreaker 회로차단기는 실패할 수 있는 작업에서 “프록시” 역할을 한다.**

- 프록시는 발생한 최근 오류수를 모니터링 하거나
- 이 정보를 사용하여 작업을 진행할 것인지 예외를 바로 반환할 것인지 판단한다.

<aside>
🎈 즉, CircuktBreaker는 Client 요청에 대해 Supplier에 대한 접근을 제한하는 프록시 역할을 수행하는 것이다.

</aside>

![image](https://user-images.githubusercontent.com/18654358/157136911-69cb5cce-d881-4686-a7be-f68492809f65.png)

---

### 예제 코드

**인터페이스를 이용한 프록시** 

[Logic.java](http://Logic.java) (interface)

```java
package com.example.proxy.pureproxy.interfaceproxy.code;

public interface Logic {
    String operation();
}
```

[RealLogic.java](http://RealLogic.java) (implements Logic)

```java
package com.example.proxy.pureproxy.interfaceproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealLogic implements Logic{
    @Override
    public String operation() {
        log.info("RealLogic 실행!");
        return "data";
    }
}
```

**InterfaceProxyClient.java**

```java
package com.example.proxy.pureproxy.interfaceproxy.code;

public class InterfaceProxyClient {

    private final Logic logic;

    public InterfaceProxyClient(Logic logic){
        this.logic = logic;
    }

    public void execute(){
        logic.operation();
    }
}
```

**테스트 (noProxy)**

```java
@Test
void noProxy(){
    RealLogic realLogic = new RealLogic();
    InterfaceProxyClient client = new InterfaceProxyClient(realLogic);

    client.execute();
}
```

<aside>
🎈 RealLogic 객체를 생성하여 Client의 Logic으로 의존성 주입을 수행하고
Client의 execute() 메소드를 호출하여 
Client가 가지고 있는 Logic 객체의 operation() 메소드를 수행한다.

</aside>

[TimeProxy.java](http://TimeProxy.java) (implements Logic)

```java
package com.example.proxy.pureproxy.interfaceproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy implements Logic{

    private final Logic target;

    public TimeProxy(Logic target){
        this.target = target;
    }

    @Override
    public String operation() {

        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();

        // 구체 클래스의 핵심 비즈니스 로직
        String operation = target.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료. resultTime = {}ms", resultTime);
        return operation;
    }
}
```

**테스트 (interfaceProxy)**

```java
@Test
void interfaceProxy(){
    RealLogic realLogic = new RealLogic();
    TimeProxy timeProxy = new TimeProxy(realLogic);
    InterfaceProxyClient client = new InterfaceProxyClient(timeProxy);

    client.execute();
}
```

<aside>
🎈 최종적으로 Client는 Logic 객체를 주입받는데 이 Logic 객체는 “TimeProxy” 이다.
TimeProxy는 RealLogic을 주입받는다.

RealLogic, TimeProxy 둘다 Logic 인터페이스를 구현한 구현체임으로 다형성을 활용하여 이와 같이 구성해낼 수 있다.

</aside>

**구체 클래스를 이용한 프록시**

ConcreteLogic.java

```java
package com.example.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteLogic {

    public String operation(){
        log.info("ConcreateLogic 실행");
        return "data";
    }
}
```

ConcreteClient.java

```java
package com.example.proxy.pureproxy.concreteproxy.code;

public class ConcreteClient {

    private ConcreteLogic concreteLogic;

    public ConcreteClient(ConcreteLogic concreteLogic){
        this.concreteLogic = concreteLogic;
    }

    public void execute(){
        concreteLogic.operation();
    }
}
```

**테스트 (noProxy)**

```java
@Test
void noProxy(){
    ConcreteLogic concreteLogic = new ConcreteLogic();
    ConcreteClient client = new ConcreteClient(concreteLogic);

    client.execute();
}
```

[TimeProxy.java](http://TimeProxy.java) (extends ConcreteLogic)

```java
package com.example.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic{

    private ConcreteLogic target;

    public TimeProxy(ConcreteLogic target){
        this.target = target;
    }

    // 구체 클래스의 핵심 비즈니스 로직을 수행하는 메소드를 Override 한다.
    @Override
    public String operation(){
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();

        // 구체 클래스의 핵심 비즈니스 로직
        String operation = target.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료. resultTime = {}ms", resultTime);
        return operation;
    }
}
```

**테스트 (concreteProxy)**
