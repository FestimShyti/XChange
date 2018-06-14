package org.knowm.xchange.coingi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.coingi.CoingiAdapters;
import org.knowm.xchange.coingi.CoingiErrorAdapter;
import org.knowm.xchange.coingi.dto.CoingiException;
import org.knowm.xchange.coingi.dto.account.CoingiBalances;
import org.knowm.xchange.coingi.dto.account.CoingiUserTransactionList;
import org.knowm.xchange.coingi.dto.account.CoingiWithdrawalRequest;
import org.knowm.xchange.coingi.dto.trade.CoingiTransactionHistoryRequest;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

public class CoingiAccountService extends CoingiAccountServiceRaw implements AccountService {
  public CoingiAccountService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    CoingiBalances coingiBalances;
    try {
      coingiBalances = getCoingiBalance();
      return CoingiAdapters.adaptAccountInfo(
          coingiBalances, exchange.getExchangeSpecification().getUserName());
    } catch (CoingiException e) {
      throw CoingiErrorAdapter.adapt(e);
    }
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, String address)
      throws IOException {
    String result;
    try {
      result = withdrawFunds(new DefaultWithdrawFundsParams(address, currency, amount));
      return result;
    } catch (CoingiException e) {
      throw CoingiErrorAdapter.adapt(e);
    }
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams p)
      throws IOException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException {
    try {
      if (p instanceof DefaultWithdrawFundsParams) {
        DefaultWithdrawFundsParams params = (DefaultWithdrawFundsParams) p;
        CoingiWithdrawalRequest request =
            new CoingiWithdrawalRequest()
                .setAddress(params.address)
                .setAmount(params.amount)
                .setCurrency(params.currency.getCurrencyCode().toUpperCase());

        return withdraw(request).toString();
      }
    } catch (CoingiException e) {
      throw CoingiErrorAdapter.adapt(e);
    }

    throw new NotYetImplementedForExchangeException();
  }

  /**
   * This returns the current deposit address. It does not generate a new one! Repeated calls will
   * return the same.
   */
  @Override
  public String requestDepositAddress(Currency currency, String... arguments) {
    throw new NotYetImplementedForExchangeException();
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    return new CoingiTradeHistoryParams(null, 1, 30, null, null);
  }

  public CoingiUserTransactionList getTransactions(TradeHistoryParams p) throws IOException {
    CoingiTradeHistoryParams params = (CoingiTradeHistoryParams) p;

    CoingiTransactionHistoryRequest request = new CoingiTransactionHistoryRequest();
    request.setPageNumber(params.getPageNumber());
    request.setCurrencyPair(params.getCurrencyPair());
    request.setPageSize(params.getPageSize());
    request.setType(params.getType());
    request.setStatus(params.getStatus());
    CoingiUserTransactionList transactions;
    try {
      transactions = getTransactions(request);
      return transactions;
    } catch (CoingiException e) {
      throw CoingiErrorAdapter.adapt(e);
    }
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params)
      throws NotAvailableFromExchangeException, NotYetImplementedForExchangeException {
    throw new NotYetImplementedForExchangeException();
  }
}
