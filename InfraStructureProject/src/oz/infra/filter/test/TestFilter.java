package oz.infra.filter.test;

import java.util.logging.Logger;

import oz.infra.filter.AcceptRejectAnySubStringFilter;
import oz.infra.filter.AcceptRejectSubStringFilter;
import oz.infra.logging.jul.JulUtils;


public class TestFilter {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testAcceptRejectSubStringFilter();
		testAcceptRejectAnySubStringFilter();
	}

	private static void testAcceptRejectSubStringFilter() {
		AcceptRejectSubStringFilter acceptSubStringFilter = new AcceptRejectSubStringFilter("xxx",
				"ACCEPT");
		logger.info(String.valueOf(acceptSubStringFilter.accept("qqqaaazzz")));
		logger.info(String.valueOf(acceptSubStringFilter.accept("qqqaaaxxxzzz")));

		AcceptRejectSubStringFilter rejectSubStringFilter = new AcceptRejectSubStringFilter("xxx",
				"REJECT");
		logger.info(String.valueOf(rejectSubStringFilter.accept("qqqaaazzz")));
		logger.info(String.valueOf(rejectSubStringFilter.accept("qqqaaaxxxzzz")));
	}

	private static void testAcceptRejectAnySubStringFilter() {
		AcceptRejectAnySubStringFilter acceptRejectAnySubStringFilter = null;
		 acceptRejectAnySubStringFilter = new AcceptRejectAnySubStringFilter(
		 "xxx", "ACCEPT");
		
		 logger.info("false " +
		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqaaazzz")));
		 logger.info("true " +
		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxxaaazzz")));
		
		 acceptRejectAnySubStringFilter = new AcceptRejectAnySubStringFilter(
		 "xxx", "REJECT");
		 logger.info("false " +
		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxxaaazzz")));
		 logger.info("true " +
		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxaaazzzx")));
		
//		 acceptRejectAnySubStringFilter = new AcceptRejectAnySubStringFilter(
//		 null, "REJECT");
//		 logger.info("true " +
//		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxxaaazzz")));
//		 logger.info("true " +
//		 String.valueOf(acceptRejectAnySubStringFilter.accept(null)));
//		
//		 acceptRejectAnySubStringFilter = new AcceptRejectAnySubStringFilter(
//		 null, "ACCEPT");
//		 logger.info("true " +
//		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxxaaazzz")));
//		 logger.info("true " +
//		 String.valueOf(acceptRejectAnySubStringFilter.accept(null)));
		
		 acceptRejectAnySubStringFilter = new AcceptRejectAnySubStringFilter(
		 "xxx,yyy", "ACCEPT");
		 logger.info("true " +
		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxaaayyyzzz")));
		 logger.info("false " +
		 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxaaayyzzz")));

		acceptRejectAnySubStringFilter = new AcceptRejectAnySubStringFilter("xxx,yyy", "REJECT");
		logger.info("false "
				+ String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxaaayyyzzz")));
		logger.info("true "
				+ String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxaaayyzzz")));
		
		 acceptRejectAnySubStringFilter = new AcceptRejectAnySubStringFilter(
				 "xxx,ZZZ,QQQ", "ACCEPT");
				 logger.info("true " +
				 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxaaaQQQyyyzzz")));
				 logger.info("false " +
				 String.valueOf(acceptRejectAnySubStringFilter.accept("qqqxxaaayyzzz")));
	}

}
