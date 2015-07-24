<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    version="2.0">
    
    <xsl:output method="html"/>
    
    <xsl:variable name="basename" select="index/@basename"/>
    
    <xsl:template match="/">
        <html>
            <head>
                <title>DANS schemas</title>
                <style><xsl:call-template name="style"/></style>
            </head>
            <body>
                <h1>Index of /schemas</h1>
                <xsl:apply-templates select="index/type"/>
                <hr/>
                <p class="small">
                    xslt transformation was done by:
                    <br />
                    Version:
                    <xsl:value-of select="system-property('xsl:version')" />
                    <br />
                    Vendor:
                    <xsl:value-of select="system-property('xsl:vendor')" />
                    <br />
                    Vendor URL:
                    <xsl:value-of select="system-property('xsl:vendor-url')" />
                </p>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="index/type">
        <h3><xsl:value-of select="@label"/></h3>
        <xsl:apply-templates select="schemas/schema"/>
    </xsl:template>
    
    <xsl:template match="schemas/schema">
        <xsl:variable name="schemaLocation">
            <xsl:value-of select="$basename"/><xsl:value-of select="schemaLocation"/>
        </xsl:variable>
        <xsl:variable name="namespace">
            <xsl:value-of select="document($schemaLocation)/xs:schema/@targetNamespace"/>
        </xsl:variable>
        <xsl:variable name="description">
            <xsl:value-of select="document($schemaLocation)/xs:schema/xs:annotation/xs:documentation/text()"/>
        </xsl:variable>
        
        <table class="ns">
            <tr>
                <td class="key">namespace</td>
                <td><xsl:value-of select="$namespace"/></td>
            </tr>
            <tr>
                <td class="key">schemaLocation</td>
                <td>
                    <xsl:element name="a">
                        <xsl:attribute name="href"><xsl:value-of select="$schemaLocation"/></xsl:attribute>
                        <xsl:attribute name="target">_blank</xsl:attribute>
                        <xsl:value-of select="$schemaLocation"/>
                    </xsl:element>
                </td>
            </tr>
            <tr>
                <td class="key">prefix (preferred)</td>
                <td><xsl:value-of select="prefix"/></td>
            </tr>
            <tr>
                <td class="key">description</td>
                <td>
                    <xsl:call-template name="replaceLinebreaks">
                        <xsl:with-param name="string" select="$description"/>
                    </xsl:call-template>
                </td>
            </tr>
            <tr>
                <td class="key">documentation</td>
                <td>
                    <xsl:apply-templates select="documentation"/>
                </td>
            </tr>
        </table>
        <br/>
    </xsl:template>
    
    <xsl:template match="documentation">
        <xsl:if test="./text() != ''">
            <xsl:variable name="link">
                <xsl:value-of select="."/>
            </xsl:variable>
            <xsl:variable name="linkText">
                <xsl:choose>
                    <xsl:when test="./@label">
                        <xsl:value-of select="./@label"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="."/>
                    </xsl:otherwise>
                </xsl:choose>                
            </xsl:variable>
             [
            <xsl:element name="a">
                <xsl:attribute name="href"><xsl:value-of select="$link"/></xsl:attribute>
                <xsl:attribute name="target">_blank</xsl:attribute>
                <xsl:value-of select="$linkText"/>
            </xsl:element>
            ] 
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="replaceLinebreaks">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="contains($string,'&#10;')">
                <xsl:value-of select="substring-before($string,'&#10;')"/>
                <br/>
                <xsl:call-template name="replaceLinebreaks">
                    <xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="style">
        h1, h2, h3
        {
        font-family: Verdana, Geneva, sans-serif;
        color: #0090B3;
        }
        .ns
        {
        font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
        border-collapse:collapse;
        width: 50em;
        }
        .ns td, .ns th 
        {
        font-size:1em;
        border:1px solid #A9E2F3;
        padding:3px 7px 2px 7px;
        vertical-align: top;
        width: 75%;
        }
        td.key 
        {
        font-size:1.1em;
        text-align:left;
        padding-top:5px;
        padding-bottom:4px;
        background-color:#0090B3;
        color:#ffffff;
        width: 25%;
        }
        .small
        {
        font-size:0.7em;
        }
        a {
        color: #00A7D4;	/* easy blauw */
        text-decoration: none!important;
        font-family: Verdana, Geneva, sans-serif!important;
        font-size: 100%;
        }
        
        a:hover {
        text-decoration: underline!important;
        color: #156083; /* easy blauw donkerder */
        }
        
        a.action {
        color:#00A7D4!important; /* DANS BLAUW */
        }
        
        a.action:hover{
        color: #3C987E;
        }
        
        a:visited {
        text-decoration: none;
        color: #3C987E;
        }
    </xsl:template>

</xsl:stylesheet>
