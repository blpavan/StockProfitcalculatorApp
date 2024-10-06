package com.stockprice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StockProfitCalculatorApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testMainMethodWithValidArguments(){
		String[] args = {"AAPL", "2023"};

		assertDoesNotThrow(() -> StockProfitCalculatorApplication.main(args));
	}

	@Test
	public void testMainMethodWithFewerArguments() {
		String[] args = {"MSFT"};

		assertThrows(IllegalArgumentException.class, () -> StockProfitCalculatorApplication.main(args));
	}

	@Test
	public void testMainMethodWithExcessArguments() {
		String[] args = {"MSFT","2023", "AAPL"};

		assertThrows(IllegalArgumentException.class, () -> StockProfitCalculatorApplication.main(args));
	}

	@Test
	public void testMainMethodWithEmptyDateArgument() {
		String[] args = {"MSFT", ""};

		assertThrows(IllegalArgumentException.class, () -> StockProfitCalculatorApplication.main(args));
	}

	@Test
	public void testMainMethodWithEmptyStockNameArgument() {
		String[] args = {"", "1997"};

		assertThrows(IllegalArgumentException.class, () -> StockProfitCalculatorApplication.main(args));
	}

	@Test
	public void testMainMethodWithEmptyArgument() {
		String[] args = {"", ""};

		assertThrows(IllegalArgumentException.class, () -> StockProfitCalculatorApplication.main(args));
	}

	@Test
	public void testMainMethodWithInvalidArguments() {
		String[] args = {"", "123"};

		assertThrows(IllegalArgumentException.class, () -> StockProfitCalculatorApplication.main(args));
	}

	@Test
	public void testMainMethodWithNullArguments() {
		String[] args = {};
		assertThrows(IllegalArgumentException.class, () -> StockProfitCalculatorApplication.main(args));
	}
}
