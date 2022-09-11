<style>
    .invitation__wrapper {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate( -50%, -50%);
    }
    .invitation__container {
        background-color: rgb(255,255,255);
        box-shadow: 0 0 5px 0 rgba(0,0,0,0.3);
        border-radius: 3px;
        width: 420px;
        height: auto;
        font-family: 'Open Sans';
        text-align: center;
    }
    .invitation__header {
        width: 100%;
        height: 60px;
        padding: 10px 0;
    }
    .invitation__logo{
        width: 250px;
    }
    .invitation__subject {
        height: 40px;
        text-align: center;
        font-size: 15px;
        font-weight: 600;
    }
    .invitation__arrow {
        position: relative;
        background: transparent;
    }
    .invitation__arrow:after {
        top: 100%;
        left: 50%;
        border: solid transparent;
        content: " ";
        height: 0;
        width: 0;
        position: absolute;
        pointer-events: none;
        border-color: rgba(255, 255, 255, 0);
        border-top-color: #fff;
        border-width: 20px;
        margin-left: -20px;
    }
    .invitation__message {
        background-color: rgb(248,248,248);
        height: 60px;
        line-height: 25px;
        padding: 30px 10px 10px;
        font-size: 15px;
    }
    .invitation__user {
        font-weight: 600;
        font-size: 18px;
    }
    .invitation__link {
        background-color: rgb(255,255,255);
        height: 100px;
        font-size: 15px;
        line-height: 25px;
        padding: 20px 10px;
    }
    .invitation__btn {
        border-radius: 3px;
        color: rgb(255,255,255);
        font-size: 15px;
        background: rgb(21,142,255);
        padding: 10px 20px;
        text-decoration: none;
        width: 200px;
        margin: 10px auto 0;
        cursor: pointer;
    }
    .invitation__btn:hover {
        background: #2997ff; /*lighten(rgb(21,142,255), 3)*/
        text-decoration: none;
    }
    .invitation__footer {
        font-size: 13px;
        line-height: 20px;
        height: 50px;
        padding: 10px 0;
    }
</style>

<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Open+Sans" />

<div class="invitation__wrapper">
    <div class="invitation__container">
        <img height="25" width="122"
             src="https://ci4.googleusercontent.com/proxy/Pnj1btnbyVGKZUuVcIzuSKhwGygiAZIpADiS6bYdAH-_07EjJ7v-D0rrU92El5qhXoaFkdRAb4_htEptcso7EqT-0O-0leJ4VQG8ZWZyqw0fPERKQFI=s0-d-e1-ft#https://dm9d104elaxz5.cloudfront.net/logo/trello-logo-color-min.png"
             title="Trello"
             style="font-weight:bold;font-size:18px;color:#0079bf;vertical-align:top"
             class="CToWUd" data-bit="iit">
        <div class="invitation__subject">  </div>
        <div class="invitation__arrow"></div>
        <div class="invitation__message">
            <div> Hey there! <span class="invitation__user">${senderFullName}</span>
                <br/>  from <span class="invitation__user">${workspace}</span> has invited you to their Workspace on Trello. </div>
        </div>
        <div class="invitation__link">

            <a href="${invitation_link}" target="_blank" class="invitation_link"> Join the Workspace</a>
        </div>
        <div class="invitation__footer">
            Trello boards help you put your plans into action and achieve your goals.
            <br/>
        </div>
    </div>
</div>