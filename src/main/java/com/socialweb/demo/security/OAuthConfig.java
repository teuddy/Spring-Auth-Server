package com.socialweb.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
//@EnableAuthorizationServer
public class OAuthConfig extends AuthorizationServerConfigurerAdapter {


    private String clientId = "talk2amareswaran";
    private String clientSecret = "my-secret";
    private String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXgIBAAKBgQD1LhJ2vh0lmrm6IHUTG0pD1eomRUjKONxsCh6jz24H013XuUKP\n" +
            "By6+XIm3r/C6gW3Uwl6bRoXiwIrR+T+P+xQ760QBFBYjsOry2xYWljpmjRYePZLk\n" +
            "20XxBfKEAQNRyZ0cG0W8f1/jvlMIvnnCAPzWS2bAsSJQX0RD5lJKgQuq8QIDAQAB\n" +
            "AoGBANa3XHAlGn1+PTzlg+IRuj4iLocJki+XEGKFkkO/Owsbbkmn8sQzdBo+DFWT\n" +
            "fWy4QKlc92Fgp4vnNyuf7qx3JEWGeD/2Jz9H8tk9AUtbffy68DEEvKqAHAgCk8iz\n" +
            "hFae3dBlbI2W+UWiFvNo8MWJEiqWWcbBTDfA7J+xZxryKe6JAkEA++Z/Z93lCIyF\n" +
            "U4p5ulh3F74NLJtTxt0U16hv+jmmTSJooX+DO3AFr9SpESsUwltV2FQtNzc4L2AC\n" +
            "GQyeBsGjTwJBAPkrky61Kkb64BvZ5mX/SKc74I0FYSddKhVZBhYgfIWV4tJcyxGl\n" +
            "iWIfnEIxzpLMxOV9MUV3+dMT+LtENYyHPb8CQQDpwC/0I33YXAWQy0xANr10w8CX\n" +
            "5x9rx1YC9WPL0FlY9JghXSFbM42jZ3Tn4JFn9beIgjxfI5e4pp/rvMCbxm6TAkAb\n" +
            "Kz8vNp3Xg39wSgAvWvinE4ZWmiGP0z7FeSGKxBwPzNogqfGSiQRD6MJ3DUtDAoTt\n" +
            "qQr4Ui6xb1oNwVA9dHmtAkEAq6AiK9QmpR+Zd9xjegfXIijiiWHOFaobqtpH1FkH\n" +
            "VZiPS2HMt6P4H1dld9/8qvtieJHWMv6zEdB38mU2lF+I3w==\n" +
            "-----END RSA PRIVATE KEY-----";
    private String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD1LhJ2vh0lmrm6IHUTG0pD1eom\n" +
            "RUjKONxsCh6jz24H013XuUKPBy6+XIm3r/C6gW3Uwl6bRoXiwIrR+T+P+xQ760QB\n" +
            "FBYjsOry2xYWljpmjRYePZLk20XxBfKEAQNRyZ0cG0W8f1/jvlMIvnnCAPzWS2bA\n" +
            "sSJQX0RD5lJKgQuq8QIDAQAB\n" +
            "-----END PUBLIC KEY-----";


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(privateKey);
        converter.setVerifierKey(publicKey);
        return converter;
    }


    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
                .accessTokenConverter(tokenEnhancer());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory().withClient(clientId).secret(passwordEncoder.encode(clientSecret)).scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(20000)
                .refreshTokenValiditySeconds(20000);

    }

}