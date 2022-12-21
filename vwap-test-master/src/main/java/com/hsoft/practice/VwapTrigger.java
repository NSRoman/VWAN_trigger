package com.hsoft.practice;

import java.util.*;
import java.util.stream.Collectors;

import com.hsoft.api.MarketDataListener;
import com.hsoft.api.PricingDataListener;
import com.hsoft.api.VwapTriggerListener;
import com.hsoft.entity.Transaction;

/**
 * Entry point for the candidate to resolve the exercise
 */
public class VwapTrigger implements PricingDataListener, MarketDataListener {

	private final VwapTriggerListener vwapTriggerListener;
	private Map<String,Stack<Transaction>> registr=new HashMap<>();

	/**
	 * This constructor is mainly available to ease unit test by not having to
	 * provide a VwapTriggerListener
	 */
	protected VwapTrigger() {
		this.vwapTriggerListener = (productId, vwap, fairValue) -> {
			// ignore
		};
	}

	public VwapTrigger(VwapTriggerListener vwapTriggerListener) {
		this.vwapTriggerListener = vwapTriggerListener;
	}

	public synchronized void createTransaction(String productId, long quantity, double price) {
		Transaction transaction = new Transaction(productId, price, quantity);
		Stack<Transaction> file;
		if (!registr.containsKey(productId)) {
			file=new Stack<>();
		}
		else {
			file=registr.get(productId);
		}
		file.push(transaction);
		registr.put(productId, file);
	}

	@Override
	public synchronized void transactionOccurred(String productId, long quantity, double price) {
		createTransaction(productId, quantity, price);
		double vwap = calculateVwap(productId);
		if (vwap > price) {
			this.vwapTriggerListener.vwapTriggered(productId, vwap, price);
		}

		// This method will be called when a new transaction is received
		// You can then perform your check
		// And, if matching the requirement, notify the event via
		// 'this.vwapTriggerListener.vwapTriggered(xxx);'
	}

	public synchronized double calculateVwap(String productId) {
		if (registr.containsKey(productId)) {
			List<Transaction> listLastFiveTransactions = registr.get(productId).stream().limit(5)
					.collect(Collectors.toList());
			double accSuperior = 0;
			double accInferior = 0;
			for (Transaction transaction : listLastFiveTransactions) {
				accSuperior += transaction.getPrice() * transaction.getQuantity();
				accInferior += transaction.getQuantity();
			}
			double vwap = accSuperior / accInferior;
			return vwap;
		}
		return 0;
	}

	@Override
	public synchronized void fairValueChanged(String productId, double fairValue) {
		double vwap = calculateVwap(productId);
		if (vwap > fairValue) {
			this.vwapTriggerListener.vwapTriggered(productId, vwap, fairValue);
		}
		// This method will be called when a new fair value is received
		// You can then perform your check
		// And, if matching the requirement, notify the event via
		// 'this.vwapTriggerListener.vwapTriggered(xxx);'
	}
}