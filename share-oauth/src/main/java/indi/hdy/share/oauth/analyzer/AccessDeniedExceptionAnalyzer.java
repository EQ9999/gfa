package indi.hdy.share.oauth.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class AccessDeniedExceptionAnalyzer extends AbstractFailureAnalyzer<RuntimeException> {
	/**
	 * logger instance
	 */
	static Logger logger = LoggerFactory.getLogger(AccessDeniedExceptionAnalyzer.class);

	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, RuntimeException cause) {
		logger.error("AccessDeniedException", cause);
		return new FailureAnalysis("access denied", "AccessDeniedException", rootFailure);
	}
}
