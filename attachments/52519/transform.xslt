<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <xsl:element name="testsuite">
      <xsl:attribute name="should-show-all-env-variables-including-the-ones-set-by-jenkins">
        <xsl:value-of select="available-environment-variables()" />
      </xsl:attribute>
      <xsl:attribute name="should-show-a-jenkins-env-variable">
        <xsl:value-of select="environment-variable('JOB_URL')" />
      </xsl:attribute>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
