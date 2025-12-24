# reservation-seat-service

## 개요
이 프로젝트는 Tickatch의 예매 좌석 서비스 프로젝트입니다.
Reservation Seat Service는 상품의 좌석 정보를 생성, 수정과 좌석의 예약 상태 관리를 담당합니다.

> 🚧 **MVP 단계** - 현재 핵심 기능 개발 중입니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| Framework | Spring Boot 3.x |
| Language | Java 17+ |
| Database | PostgreSQL |
| Messaging | RabbitMQ |
| Query | QueryDSL |
| Communication | OpenFeign |
| Security | Spring Security |

## 아키텍처

### 시스템 구성

```
┌─────────────────────────────────────────────────────────────┐
│                        Tickatch Platform                    │
├─────────────┬─────────────┬─────────────┬───────────────────┤
│   Product   │ Reservation │   Ticket    │  ReservationSeat  │
│   Service   │   Service   │   Service   │      Service      │
└──────┬──────┴──────┬──────┴──────┬──────┴─────────┬─────────┘
       │             │             │                │
       └─────────────┴──────┬──────┴────────────────┘
                            │
                      RabbitMQ
```

### 레이어 구조

```
product-service
├── global                  # 공통
│         ├── config
│         └── domain
└── reservationseat
    ├── application         # 서비스 레이어
    │         ├── dto
    │         └── service
    ├── domain              # 엔티티, VO, 리포지토리 인터페이스
    │         ├── dto
    │         ├── exception
    │         └── vo
    ├── infrastructure      # 외부 연동, 외부 기술 구현
    └── presentation        # API 컨트롤러, DTO
        ├── api
        └── dto
```

## 주요 기능

### 1. 예매 좌석 생성 및 관리
- 좌석 생성: 상품에 대한 다수의 예매 좌석을 일괄 생성
- 좌석 정보 수정: 좌석 등급 및 가격 정보 변경
- 좌석 조회: 상품별 예매 좌석 목록 조회
- 좌석 삭제: 상품 삭제 이벤트 수신 시 자동 삭제

**활용 기술**: Spring Data JPA, QueryDSL, RabbitMQ Listener

### 2. 좌석 상태 관리
   좌석의 생명주기를 관리하며, 각 상태 전환 시 권한 검증을 수행합니다.

| 상태        | 설명    | 전환 조건              |
|-----------|-------|--------------------|
| AVAILABLE | 예매 가능 | 초기 상태 또는 취소 후      |
| PREEMPT   | 선점됨   | AVAILABLE 상태에서만 가능 |
| RESERVED  | 예약됨   | PREEMPT 상태에서만 가능   |

**활용 기술**: 상태 패턴, 도메인 주도 설계

### 3. 동시성 제어
   비관적 락(Pessimistic Lock)을 사용하여 동시 예약 환경에서 데이터 정합성을 보장합니다.

- `findByIdWithLock` 메서드를 통한 트랜잭션 레벨 잠금
- 선점/예약/취소 시 락을 획득하여 동시 접근 차단

**활용 기술**: JPA @Lock(LockModeType.PESSIMISTIC_WRITE)

### 4. 이벤트 기반 통합
   좌석 상태 변경 시 도메인 이벤트를 발행하여 다른 서비스와 느슨하게 결합됩니다.

- 발행 이벤트:
  - `ReservationSeatPreemptEvent`: 좌석 선점 시
  - `ReservationSeatCanceledEvent`: 좌석 취소 시
- 수신 이벤트:
  - 상품 삭제 이벤트 수신 → 해당 상품의 모든 좌석 삭제

**활용 기술**: RabbitMQ, Event-Driven Architecture

## API 명세

### 예매 좌석 API

| Method | Endpoint                                                | 설명              |
|--------|---------------------------------------------------------|-----------------|
| GET | `/api/v1/reservation-seats`                             | 상품의 예매 좌석 목록 조회 |
| POST | `/api/v1/internal/reservation-seats`                    | 예매 좌석 생성        |
| PUT | `/api/v1/reservation-seats`                             | 예매 좌석 정보 수정     |
| POST | `/api/v1/reservation-seats/{reservationSeatId}/preempt` | 예매 좌석 선점        |
| POST | `/api/v1/reservation-seats/{reservationSeatId}/reserve` | 예매 좌석 예약 확정     |
| POST | `/api/v1/reservation-seats/{reservationSeatId}/cancel`  | 예매/선점 좌석 예약 취소  |

## 실행 방법

### 환경 변수

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tickatch
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  rabbitmq:
    host: ${RABBITMQ_HOST:localhost}
    port: ${RABBITMQ_PORT:5672}
    username: ${RABBITMQ_USERNAME}
    password: ${RABBITMQ_PASSWORD}
```

### 실행

```bash
# 개발 환경
./gradlew bootRun

# 프로덕션 빌드
./gradlew clean build
java -jar build/libs/reservation-seat-service-*.jar
```

### Docker 실행

```bash
docker-compose up -d
```

## 데이터베이스 스키마

### p_reservation_seat

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT | PK |
| product_id | BIGINT | 상품 ID |
| seat_number | VARCHAR | 좌석 번호 |
| grade | VARCHAR | 좌석 등급 |
| price | DECIMAL | 가격 |
| status | VARCHAR | 상태 (AVAILABLE, PREEMPT, RESERVED) |
| reserver_id | UUID | 예약자 ID |
| created_at | TIMESTAMP | 생성일시 |
| updated_at | TIMESTAMP | 수정일시 |

## 이벤트 명세

### 발행 이벤트

#### ReservationSeatPreemptEvent
- **Payload**:
```json
{
  "productId": 123,
  "grade": "VIP",
  "count": 1
}
```

#### ReservationSeatCanceledEvent
- **Payload**:
```json
{
  "productId": 123,
  "grade": "VIP",
  "count": 1
}
```

### 수신 이벤트

#### ProductReservationSeatDeleteRequest
- **Payload**:
```json
{
  "productId": 123
}
```

## 관련 서비스

- **Product Service** - 상품 관리 및 좌석 생성 요청
- **Reservation Service** - 예매 생성 및 좌석 선점/예약
- **Ticket Service** - 티켓 발권

## 트러블슈팅

### 동시성 이슈
- **문제**: 동일 좌석에 대한 동시 예약 시도
- **해결**: Pessimistic Lock 적용으로 트랜잭션 레벨 잠금 보장

### 데드락 방지
- 좌석 ID 순서로 정렬하여 락 획득 순서 일관성 유지


---

© 2025 Tickatch Team