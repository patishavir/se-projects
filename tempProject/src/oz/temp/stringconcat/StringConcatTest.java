package oz.temp.stringconcat;

import java.util.logging.Logger;

import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class StringConcatTest {

	static final long loopLimit = 10000000l;
	static final String[] s = { "uiqow8yf9p8qwy89py", "po9u908-90uiohnkjn", "80987iohjnuit7856456tyvhv",
			"poi9up890u9p8ojh;hiop8i", "likjioup98uopjhi;iy89yiuhl;ho;uiljiiou8opu",
			"oliopiupoiup8907234890uoijho;uj4652554", "loj;oiup90u0pr9up90796ogkjgbjkuytf6u5", "90u8p98uoijo",
			"iuoiy78678jhbjtkygg", "oiupiou9p87u89khuio", "o8up8up98ph", "oup9o8up89upou898ui", "889789yijklhvyy",
			"ou889uhkbjklyo", "98798ut7867ybkloo", "987ohj67uyt6562okjk", "80897887y8y", "lkhiuyo78bhbuyguyuy",
			"99887y7y#&*)))", "kiopuiulhu7&&*TGFRFJNN", "098uiuyuouui9%$FGUIQ!#" };
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StopWatch stopWatch1 = new StopWatch();
		for (long i = 0; i < loopLimit; i++) {
			concat1();
		}
		stopWatch1.logElapsedTimeMessage();

		StopWatch stopWatch2 = new StopWatch();
		for (long i = 0; i < loopLimit; i++) {
			concat2();
		}
		stopWatch2.logElapsedTimeMessage();
	}

	public static void concat1() {
		String st1 = s[0] + s[1] + s[2] + s[3] + s[4] + s[5] + s[6] + s[7] + s[8] + s[9] + s[10] + s[11] + s[12] + s[13]
				+ s[14] + s[15] + s[16] + s[17] + s[18] + s[19] + s[20];
		logger.finest(st1);
	}

	public static void concat2() {
		String st1 = StringUtils.concat(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[9], s[10], s[11], s[12],
				s[13], s[14], s[15], s[16], s[17], s[18], s[19], s[20]);
		logger.finest(st1);
	}

}
