package com.stephen.workqueue;

import com.rabbitmq.client.Channel;
import com.stephen.util.RabbitMqUtils;

/**
 * 为了保证消息在发送过程中不丢失，rabbitmq 引入消息应答机制，消息应答就是:消费者在接收
 * 到消息并且处理该消息之后，告诉 rabbitmq 它已经处理了，rabbitmq 可以把该消息删除了。
 *
 * 如果消费者由于某些原因失去连接(其通道已关闭，连接已关闭或 TCP 连接丢失)，导致消息
 * 未发送 ACK 确认，RabbitMQ 将了解到消息未完全处理，并将对其重新排队。如果此时其他消费者
 * 可以处理，它将很快将其重新分发给另一个消费者
 */
import java.util.Scanner;

public class Task02 {

    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] argv) throws Exception {

        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, false, false, false,
                    null);
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");
            while (sc.hasNext()) {
                String message = sc.nextLine();
                channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
                System.out.println("生产者发出消息" + message);
            }
        }
    }
}