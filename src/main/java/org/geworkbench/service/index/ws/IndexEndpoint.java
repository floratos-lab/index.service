package org.geworkbench.service.index.ws;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geworkbench.service.index.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

@Endpoint
public class IndexEndpoint {
    /**
     * Namespace of both request and response.
     */
    public static final String NAMESPACE_URI = "http://www.geworkbench.org/service/index";

    /**
     * The local name of the expected request.
     */
    public static final String INDEX_REQUEST_LOCAL_NAME = "indexRequest";

    /**
     * The local name of the created response.
     */
    public static final String INDEX_RESPONSE_LOCAL_NAME = "indexResponse";

    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    private final IndexService indexService;

    @Autowired
    public IndexEndpoint(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * Reads the given <code>requestElement</code>, and sends a the response back.
     *
     * @param requestElement the contents of the SOAP message as DOM elements
     * @return the response element
     */
    @PayloadRoot(localPart = INDEX_REQUEST_LOCAL_NAME, namespace = NAMESPACE_URI)
    @ResponsePayload
    public Element handleIndexRequest(@RequestPayload Element requestElement) throws ParserConfigurationException {
        Assert.isTrue(NAMESPACE_URI.equals(requestElement.getNamespaceURI()), "Invalid namespace");
        Assert.isTrue(INDEX_REQUEST_LOCAL_NAME.equals(requestElement.getLocalName()), "Invalid local name");

        NodeList children = requestElement.getChildNodes();
        Text requestText = null;
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.TEXT_NODE) {
                requestText = (Text) children.item(i);
                break;
            }
        }
        if (requestText == null) {
            throw new IllegalArgumentException("Could not find request text node");
        }

        String url = indexService.find(requestText.getNodeValue());

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, INDEX_RESPONSE_LOCAL_NAME);
        Text responseText = document.createTextNode(url);
        responseElement.appendChild(responseText);
        return responseElement;
    }
}
