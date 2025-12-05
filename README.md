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
product-service/
├── presentation/       # API 컨트롤러, DTO
│   ├── api/
│   └── dto/
├── application/        # 서비스 레이어
│   ├── dto/
│   └── service/
├── domain/             # 엔티티, VO, 리포지토리 인터페이스
│   ├── dto/
│   ├── exception/
│   └── vo/
├── infrastructure/     # 외부 연동, 외부 기술 구현
└── global/             # 공통
```

## 주요 기능

### 예매 좌석 관리
- 예매 좌석 생성 / 예매 좌석 정보 수정 / 상품 예매 좌석 조회
- 예매 좌석의 예약 / 선점 / 취소

### 예매 좌석 상태
| 상태 | 설명 |
|------|------|
| AVAILABLE | 예매 가능 |
| PREEMPT | 선점됨 |
| RESERVED | 예약됨 |

## API 명세

### 예매 좌석 API

| Method | Endpoint | 설명              |
|--------|----------|-----------------|
| GET | `/api/v1/reservation-seats` | 상품의 예매 좌석 목록 조회 |
| POST | `/api/v1/reservation-seats` | 예매 좌석 생성        |
| PUT | `/api/v1/reservation-seats` | 예매 좌석 정보 수정     |
| POST | `/api/v1/reservation-seats/{reservationSeatId}/preempt` | 예매 좌석 선점        |
| POST | `/api/v1/reservation-seats/{reservationSeatId}/reserve` | 예매 좌석 예약 확정     |
| POST | `/api/v1/reservation-seats/{reservationSeatId}/cancel` | 예매/선점 좌석 예약 취소  |

## 실행 방법

### 환경 변수

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tickatch
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  rabbitmq:
    host: localhost
    port: 5672
    username: ${RABBITMQ_USERNAME}
    password: ${RABBITMQ_PASSWORD}
```

### 실행

```bash
./gradlew bootRun
```

## 관련 서비스

- **Reservation Service** - 예매 관리
- **Ticket Service** - 티켓 발권
- **Product Service** - 상품 관리

---

© 2025 Tickatch Team