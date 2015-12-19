package com.xeiam.xchange.huobi.service.polling;

import static com.xeiam.xchange.dto.Order.OrderType.BID;

import java.io.IOException;
import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.huobi.dto.trade.HuobiCancelOrderResult;
import com.xeiam.xchange.huobi.dto.trade.HuobiOrder;
import com.xeiam.xchange.huobi.dto.trade.HuobiOrderInfo;
import com.xeiam.xchange.huobi.dto.trade.HuobiPlaceOrderResult;
import com.xeiam.xchange.huobi.service.TradeServiceRaw;

public class HuobiTradeServiceRaw extends HuobiBaseTradeService implements TradeServiceRaw {

  /**
   * Constructor
   *
   * @param exchange
   */
  public HuobiTradeServiceRaw(Exchange exchange) {
    super(exchange);
  }

  @Override
  public HuobiOrder[] getOrders(int coinType) throws IOException {
    return huobi.getOrders(accessKey, coinType, nextCreated(), "get_orders", digest);
  }

  @Override
  public HuobiOrderInfo getOrderInfo(long orderId, int coinType) throws IOException {
    return huobi.getOrderInfo(accessKey, orderId, coinType, nextCreated(), "order_info", digest);
  }

  @Override
  public HuobiPlaceOrderResult placeLimitOrder(OrderType type, int coinType, BigDecimal price, BigDecimal amount) throws IOException {
    String method = type == BID ? "buy" : "sell";

    return huobi.placeLimitOrder(accessKey, amount.toPlainString(), coinType, nextCreated(), price.toPlainString(), method, digest);
  }

  @Override
  public HuobiPlaceOrderResult placeMarketOrder(OrderType type, int coinType, BigDecimal amount) throws IOException {
    final String method = type == BID ? "buy_market" : "sell_market";

    return huobi.placeMarketOrder(accessKey, amount.toPlainString(), coinType, nextCreated(), method, digest);
  }

  @Override
  public HuobiCancelOrderResult cancelOrder(int coinType, long id) throws IOException {
    return huobi.cancelOrder(accessKey, coinType, nextCreated(), id, "cancel_order", digest);
  }
}