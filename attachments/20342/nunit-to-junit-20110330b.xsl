<?xml version="1.0" encoding="UTF-8"?>
<!-- By Eamon Nerbonne (Rijksuniversiteit Groningen) -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" />

  <xsl:template match="test-suite" mode="name">
    <xsl:variable name="parent" select="ancestor::test-suite[1]"/>
    <xsl:choose>
      <xsl:when test="$parent/@type = 'ParameterizedFixture' and starts-with(@name,$parent/@name)">
        <xsl:apply-templates select="$parent" mode="name" />
        <xsl:value-of select="substring(@name,string-length($parent/@name)+1)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="$parent" mode="nameWithDot" />
        <xsl:value-of select="@name"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="test-suite" mode="nameWithDot">
    <xsl:if test="not(@type = 'Assembly' or @type = 'Project' or (not(@type) and count(/test-results/test-suite) = count(.|/test-results/test-suite) ))">
      <xsl:apply-templates select="." mode="name" />
      <xsl:text>.</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="/test-results">
    <testsuites>
      <xsl:for-each select="//test-suite[results/test-case]">
        <xsl:variable name="fulltestsuitename"><xsl:apply-templates select="." mode="name"/></xsl:variable>
        
        <testsuite name="{$fulltestsuitename}" tests="{count(*/test-case)}" time="{@time}" failures="{count(*/test-case/failure)}" errors="0" skipped="{count(*/test-case[@executed='False'])}">

          <xsl:for-each select="*/test-case">
            <xsl:variable name="testcaseName">
              <xsl:choose>
                <xsl:when test="starts-with(@name, concat($fulltestsuitename,'.'))">
                  <xsl:value-of select="substring(@name, string-length($fulltestsuitename)+2)"/>
                </xsl:when>
                <xsl:when test="starts-with(@name, concat($fulltestsuitename,'('))">
                  <xsl:value-of select="concat(ancestor::test-suite[1]/@name,'(', substring(@name, string-length($fulltestsuitename)+2))"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="@name"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:variable>

            <xsl:choose>
              <xsl:when test="@executed='False'">
                <testcase classname="{$fulltestsuitename}" name="{$testcaseName}">
                  <skipped message="{reason/message}"/>
                </testcase>
              </xsl:when>
              <xsl:otherwise>
                <testcase classname="{$fulltestsuitename}" name="{$testcaseName}" time="{@time}">
                  <xsl:apply-templates select="failure"/>
                </testcase>
              </xsl:otherwise>
            </xsl:choose>
            
          </xsl:for-each>
        </testsuite>
      </xsl:for-each>
    </testsuites>
  </xsl:template>

  <xsl:template match="failure">
    <failure>
      MESSAGE:
      <xsl:value-of select="message" />
      +++++++++++++++++++
      STACK TRACE:
      <xsl:value-of select="stack-trace" />
    </failure>
  </xsl:template>
</xsl:stylesheet>
