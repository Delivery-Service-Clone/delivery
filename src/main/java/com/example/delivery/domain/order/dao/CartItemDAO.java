package com.example.delivery.domain.order.dao;

import com.example.delivery.domain.order.dto.CartItemDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/*
   레디스에서의 트랜잭션은 RDB의 트랜잭션과는 다르다. 레디스의 트랜잭션은 rollback기능이 없다.
   레디스 트랜잭션에서의 오류는 잘못된 명령어나 데이터타입을 실수로 쓸 때만 나기 때문에 롤백을 전부다 하지않고
   exec 후에 오류가 나지 않은 부분은 실행된다.
   exec 이전에 command queue에 적재하는 도중 실패하는 경우 (command 문법오류,메모리 부족오류등,
   다른 클라이언트에서 command날려 atomic보장이 안되는 경우) 에는 exec하면 전부 discard된다.
   (실험해보니 multi 후 트랜잭션중 다른 스레드에서 command를 날리면 discard된다.)
   (레디스 2.6.5이후로 트랜잭션시작 후 오류가 있으면 exec될 때 전부 discard된다.)
   트랜잭션 명령어들은 exec되기 위해 큐에서 기다리는데 discard를 이용해 실행을 하지 않을 수 있다.
   트랜잭션의 locking은 watch를 이용한 optimistic locking이다. watch로 어떠한 키를 감시하고
   이 키의 트랜잭션이 multi로 시작되기전에 watch할 때의 값과 multi할 때의 값이 변경이 없어야지
   트랜잭션이 시작할 수 있다. 만약에 이 값이 변경이 되었다면 race condition이 일어난 것이기 때문에
   트랜잭션 에러가 난다.
*/

@Repository
@RequiredArgsConstructor
public class CartItemDAO {

  private final RedisTemplate<String, CartItemDTO> redisTemplate;
  private static final String cartKey = ":CART";

  public static String generateCartKey(String id) {
    return id + cartKey;
  }

  public List<CartItemDTO> selectCartList(String userId) {
    final String key = generateCartKey(userId);

    List<CartItemDTO> cartList = redisTemplate.opsForList().range(key, 0, -1);

    return cartList;
  }

  /*
    rightPush를 하는 이유
    1. 시간 순서 보존
      장바구니에 항목이 추가되는 순서를 유지하고자 할 때, 오른쪽 끝에 요소를 추가하는 것이 유용합니다.
      사용자가 장바구니에 물건을 담는 순서대로 리스트에 추가되며, 나중에 리스트를 조회할 때 처음부터 순서대로 읽으면,
      추가된 순서대로 데이터를 얻을 수 있습니다.
    2. FIFO 구조:
      오른쪽 끝에 요소를 추가하고, 왼쪽 끝에서 요소를 꺼내는 방식으로 FIFO(First-In-First-Out) 구조를 유지할 수 있습니다.
      예를 들어, 특정 상황에서 장바구니에 오래된 항목을 제거하는 기능이 필요할 때,
      왼쪽에서 제거하면 가장 오래된 항목이 먼저 제거됩니다.
  */
  public void insertMenu(String userId, CartItemDTO cart) {
    final String key = generateCartKey(userId);
    redisTemplate.opsForList().rightPush(key, cart);
  }

  public void deleteMenuList(String userId) {
    final String key = generateCartKey(userId);
    redisTemplate.delete(key);
  }
}
