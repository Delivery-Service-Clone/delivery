package com.example.delivery.domain.rider.dao;

import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.entity.Rider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryDao {

  @Qualifier("deliveryRedisTemplate")
  private final RedisTemplate<String, Object> redisTemplate;

  private static final String STANDBY_ORDERS_KEY = "STANDBY_ORDERS:";
  private static final String STANDBY_RIDERS_KEY = "STANDBY_RIDERS:";

  private static String generateRiderHashKey(Long riderId) {
    return String.valueOf(riderId);
  }

  private static String generateOrderHashKey(Long orderId) {
    return String.valueOf(orderId);
  }

  private static String generateStandbyRiderKey(String address) {
    return STANDBY_RIDERS_KEY + address;
  }

  private static String generateStandbyOrderKey(String address) {
    return STANDBY_ORDERS_KEY + address;
  }

  // 출근시 라이더 등록
  public void registerRiderWhenStartWork(DeliveryRiderDTO riderDto, Rider rider) {

    redisTemplate
        .opsForHash()
        .put(
            generateStandbyRiderKey(riderDto.getAddress()),
            generateRiderHashKey(rider.getId()),
            riderDto.getFcmToken());
  }

  public void deleteRider(DeliveryRiderDTO riderDto, Rider rider) {
    redisTemplate
        .opsForHash()
        .delete(
            generateStandbyRiderKey(riderDto.getAddress()), generateRiderHashKey(rider.getId()));
  }

  public void insertStandbyOrder(Long orderId, OrderReceiptDto orderReceipt) {
    redisTemplate
        .opsForHash()
        .put(
            generateStandbyOrderKey(orderReceipt.getUserInfo().getAddress()),
            generateOrderHashKey(orderId),
            orderReceipt);
  }

  // 같은 지역의 출근한 라이더들에게 메시지를 보내기 위한 Token값 조회하기
  public Set<String> getRiderTokensByAddress(String address) {

    String key = generateStandbyRiderKey(address);

    return redisTemplate.execute(
        new RedisCallback<Set<String>>() {
          @Override
          public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
            ScanOptions scanOptions = ScanOptions.scanOptions().match("*").count(100).build();
            Cursor<Entry<byte[], byte[]>> cursor =
                connection.hScan(redisTemplate.getStringSerializer().serialize(key), scanOptions);

            Set<String> result = new HashSet<>();

            while (cursor.hasNext()) {
              Entry<byte[], byte[]> entry = cursor.next();
              result.add((String) redisTemplate.getValueSerializer().deserialize(entry.getValue()));
            }

            return result;
          }
        });
  }

  // 라이더 배달 가능한 지역 리스트 조회
  public List<String> selectOrderList(String address) {

    String key = generateStandbyOrderKey(address);

    return redisTemplate.execute(
        new RedisCallback<List<String>>() {
          @Override
          public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
            ScanOptions scanOptions = ScanOptions.scanOptions().match("*").count(100).build();
            Cursor<Entry<byte[], byte[]>> cursor =
                connection.hScan(redisTemplate.getStringSerializer().serialize(key), scanOptions);

            List<String> result = new ArrayList<>();

            while (cursor.hasNext()) {
              Entry<byte[], byte[]> entry = cursor.next();
              result.add(redisTemplate.getStringSerializer().deserialize(entry.getKey()));
            }

            return result;
          }
        });
  }

  // 라이더가 주문을 수락 할 때 사용되는 메서드
  // redis에서 order 및 rider 빼기
  // 이미 배달을 하고 있다면? order만 빼기
  public void updateOrderToDelivering(Long orderId, DeliveryRiderDTO riderDto, Rider rider) {
    String orderKey = generateStandbyOrderKey(riderDto.getAddress());
    String riderKey = generateStandbyRiderKey(riderDto.getAddress());
    String orderHashKey = generateOrderHashKey(orderId);
    String riderHashKey = generateRiderHashKey(rider.getId());

    // Redis에서 해당 라이더가 있는지 확인
    Boolean riderExists = redisTemplate.opsForHash().hasKey(riderKey, riderHashKey);

    if (Boolean.TRUE.equals(riderExists)) {
      // 라이더가 존재하면 라이더 정보 삭제
      redisTemplate.opsForHash().delete(riderKey, riderHashKey);
      redisTemplate.opsForHash().delete(orderKey, orderHashKey);
    } else {
      // 라이더가 존재하지 않으면 orderKey의 hashKey만 삭제
      redisTemplate.opsForHash().delete(orderKey, orderHashKey);
    }
  }
}
