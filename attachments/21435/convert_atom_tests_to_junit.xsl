<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="yes" />	
	<xsl:template match="/">
			<xsl:variable name="buildName" select="//rest/*/test_name"/>	
			<xsl:variable name="numberOfTests" select="count(//rest/*/iter_num)"/>
 			<xsl:variable name="numberOfFailures" select="count(//rest/*/status [.= 'Fail'])" />
 			<!-- TODO: update the skipped count if we change the test status -->	
 			<xsl:variable name="numberSkipped" select="count(//rest/*/status [.!='Pass' and .!='Fail'])" />	
 			<!-- read logfile path from the results.xml file -->
 			<xsl:variable name="logfile" select="//rest/@logfile" />	
 			<xsl:variable name="testfile" select="//rest/@testFile" />
 			<!-- read the buginfo file -->
 			<xsl:variable name="buginfoXml" select="fn:document('/var/tmp/qe_tests/templates/known_bugs.xml')" />
 			<!-- read the integration known issues file -->
 			<xsl:variable name="integIssuesXml" select="fn:document('/var/tmp/qe_tests/templates/integ_known_issues.xml')" />
 			
			<testsuite name="QE AUTOMATION TESTS"
				tests="{$numberOfTests}" time="0"
				failures="{$numberOfFailures}"  errors="0"
				skipped="{$numberSkipped}">
				
				<xsl:for-each select="//rest/*">
					<xsl:variable name="testName" select="test_name"/>
					<xsl:variable name="executionId" select="iter_num"/>
					<xsl:variable name="iter_log" select="concat($logfile,'-',$executionId)"/>
					<xsl:variable name="start_time" select="fn:replace(start_time,' ','T')" />
					<xsl:variable name="end_time" select="fn:replace(end_time,' ','T')"/>
					<xsl:variable name="test_parameters" select="test_parameters"/>
					<xsl:variable name="test_positive" select="test_positive"/>
					<xsl:variable name="time_diff" select="xs:dateTime($end_time)-xs:dateTime($start_time)"/>
					<xsl:variable name="duration_seconds" select="seconds-from-duration($time_diff)"/>
					<xsl:variable name="duration_minutes" select="minutes-from-duration($time_diff)"/>	
					<xsl:variable name="duration_hours" select="hours-from-duration($time_diff)"/>		
					<xsl:variable name="outcome" select="status"/>	
					<xsl:variable name="message" select="$buildName"/>	
					<xsl:choose>
						<xsl:when test="contains($outcome, 'Fail')"> 					
							<!-- insert bug info according to test name -->	
							<xsl:variable name="knownIssue" select="$buginfoXml/bugs/bug/test_name [.= $testName]"/>	
							<xsl:variable name="testId" select="$buginfoXml/bugs/bug/id [../test_name = $testName]"/>
							<xsl:variable name="integIssue" select="$integIssuesXml/issues/issue/test_name [.= $testName]"/>	
							<xsl:variable name="issueText" select="$integIssuesXml/issues/issue/test_text [../test_name = $testName]"/>	
							<xsl:variable name="issueId" select="$integIssuesXml/issues/issue/id [../test_name = $testName]"/>	
							<xsl:choose>
								<xsl:when test="$testName=$knownIssue">
										<testcase classname="automation.{local-name(.) }"
											name="{$testName}  (Bug {$testId})"
											time="{$duration_hours*3600 + $duration_minutes*60 + $duration_seconds }">
										<xsl:element name="skipped"></xsl:element>
										<xsl:element name="system-out">
	Known Issue. #<xsl:value-of select="$testId"/>
	Bug url: https://bugzilla.redhat.com/<xsl:value-of select="$testId"/>
										</xsl:element>
										</testcase>
								</xsl:when>
								<xsl:when test="$testName=$integIssue">
										<testcase classname="automation.{local-name(.) }"
											name="{$testName}  (Known Issue: {$issueId})"
											time="{$duration_hours*3600 + $duration_minutes*60 + $duration_seconds }">
										<xsl:element name="skipped"></xsl:element>
										<xsl:element name="system-out">
	Known Issue. #<xsl:value-of select="$issueId"/>
	Issue Text: <xsl:value-of select="$issueText"/>
										</xsl:element>
										</testcase>
								</xsl:when>
								<xsl:otherwise>
									<testcase classname="automation.{local-name(.) }"
											name="{$testName}"
											time="{$duration_hours*3600 + $duration_minutes*60 + $duration_seconds }">
									<xsl:element name="failure">
	<xsl:value-of select="unparsed-text($iter_log)" />	
									</xsl:element>
									</testcase>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<testcase classname="automation.{local-name(.) }"
									name="{$testName}"
									time="{$duration_hours*3600 + $duration_minutes*60 + $duration_seconds }">
							</testcase>
						</xsl:otherwise>
				       </xsl:choose>
				</xsl:for-each>
			</testsuite>
	</xsl:template>
</xsl:stylesheet>
