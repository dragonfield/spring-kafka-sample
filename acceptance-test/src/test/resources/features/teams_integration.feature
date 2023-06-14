# language: ja

機能: Teamsメッセージ通知機能

  シナリオ:条件に合うメッセージがTeamsに連携される
    前提TeamsのIncomingWebhookが稼働している
    もしトピックに対象のメッセージが送信される
    ならばTeamsのIncomingWebhook経由でチャネルにメッセージが送信される
