package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import io.github.tickatch.common.util.JsonUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 설정 클래스.
 *
 * <p>상품 이벤트 관련 예매 좌석 메시지 처리 및 DLQ(Dead Letter Queue) 구성을 포함한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Configuration
public class RabbitMQConfig {

  @Value("${messaging.product.exchange:tickatch.product}")
  private String productExchange;

  public static final String QUEUE_PRODUCT_CANCELLED_RESERVATION_SEAT =
      "tickatch.product.cancelled.reservation-seat.queue";

  public static final String ROUTING_KEY_CANCELLED_RESERVATION_SEAT =
      "product.cancelled.reservation-seat";

  /**
   * 상품 관련 이벤트를 처리하는 Topic Exchange 설정.
   *
   * @return 상품 이벤트용 Exchange
   */
  @Bean
  public TopicExchange productExchange() {
    return ExchangeBuilder.topicExchange(productExchange).durable(true).build();
  }

  /**
   * 상품 삭제 시 예매 좌석을 삭제하기 위한 메시지 큐를 생성한다.
   *
   * <p>DLX 설정을 포함하여 실패 시 Dead Letter Queue로 이동할 수 있도록 구성한다.
   *
   * @return 예매 좌석 삭제용 큐
   */
  @Bean
  public Queue productCancelledReservationSeatQueue() {
    return QueueBuilder.durable(QUEUE_PRODUCT_CANCELLED_RESERVATION_SEAT)
        .withArgument("x-dead-letter-exchange", productExchange + ".dlx")
        .withArgument("x-dead-letter-routing-key", "dlq." + ROUTING_KEY_CANCELLED_RESERVATION_SEAT)
        .build();
  }

  /**
   * 상품 삭제 관련 메시지를 큐와 Exchange에 바인딩한다.
   *
   * @param productCancelledReservationSeatQueue 예매 좌석 삭제 큐
   * @param productExchange 상품 이벤트 처리 Exchange
   * @return 바인딩 객체
   */
  @Bean
  public Binding productCancelledReservationSeatBinding(
      Queue productCancelledReservationSeatQueue, TopicExchange productExchange) {
    return BindingBuilder.bind(productCancelledReservationSeatQueue)
        .to(productExchange)
        .with(ROUTING_KEY_CANCELLED_RESERVATION_SEAT);
  }

  /**
   * 상품 이벤트 Dead Letter Exchange 설정.
   *
   * @return Dead Letter Exchange
   */
  @Bean
  public TopicExchange deadLetterExchange() {
    return ExchangeBuilder.topicExchange(productExchange + ".dlx").durable(true).build();
  }

  /**
   * Dead Letter 상태의 예매 좌석 메시지를 처리하기 위한 큐 생성.
   *
   * @return Dead Letter Queue
   */
  @Bean
  public Queue deadLetterReservationSeatQueue() {
    return QueueBuilder.durable(QUEUE_PRODUCT_CANCELLED_RESERVATION_SEAT + ".dlq").build();
  }

  /**
   * Dead Letter 메시지를 처리하기 위한 바인딩 설정.
   *
   * @param deadLetterReservationSeatQueue DLQ 큐
   * @param deadLetterExchange DLX Exchange
   * @return DLQ 바인딩 객체
   */
  @Bean
  public Binding deadLetterReservationSeatBinding(
      Queue deadLetterReservationSeatQueue, TopicExchange deadLetterExchange) {
    return BindingBuilder.bind(deadLetterReservationSeatQueue)
        .to(deadLetterExchange)
        .with("dlq." + ROUTING_KEY_CANCELLED_RESERVATION_SEAT);
  }

  /**
   * 메시지 직렬화/역직렬화를 위한 JSON 변환기 설정.
   *
   * @return JSON 메시지 컨버터
   */
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter(JsonUtils.getObjectMapper());
  }

  /**
   * RabbitMQ 메시지 송신을 위한 템플릿 설정.
   *
   * @param connectionFactory RabbitMQ 연결 팩토리
   * @param jsonMessageConverter 메시지 직렬화 컨버터
   * @return 구성된 RabbitTemplate
   */
  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter);
    return rabbitTemplate;
  }
}
