import { Divider } from 'antd';
import { createStyles } from 'antd-style';
import React from 'react';

const REPO_URL = 'https://github.com/lilemy/mora-account-book';
const COMMIT_HASH = process.env.COMMIT_HASH || '';

const useStyles = createStyles(({ token, css }) => ({
  footer: css`
    padding: 16px 24px;
    text-align: center;
    color: ${token.colorTextDescription};
    font-size: ${token.fontSizeSM}px;
    line-height: ${token.lineHeight};
    background: transparent;
  `,
  copyright: css`
    margin-bottom: 6px;
  `,
  link: css`
    color: ${token.colorTextDescription};
    text-decoration: none;
    transition: color ${token.motionDurationMid};

    &:hover {
      color: ${token.colorText};
    }
  `,
  meta: css`
    display: flex;
    align-items: center;
    justify-content: center;
    flex-wrap: wrap;
    gap: 6px 12px;
    font-family: ${token.fontFamilyCode};
    font-size: ${token.fontSizeSM - 1}px;
  `,
  group: css`
    display: inline-flex;
    align-items: center;
    gap: 4px;
  `,
  label: css`
    color: ${token.colorTextQuaternary};
  `,
  divider: css`
    display: inline-block;
    vertical-align: middle;
  `,
}));

const Footer: React.FC = () => {
  const { styles } = useStyles();
  const year = new Date().getFullYear();

  return (
    <div className={styles.footer}>
      <div className={styles.copyright}>lilemy &copy; {year}</div>
      <div className={styles.meta}>
        <span className={styles.group}>
          <span className={styles.label}>ver</span>
          <a
            className={styles.link}
            href={REPO_URL}
            target="_blank"
            rel="noopener noreferrer"
          >
            {__APP_VERSION__}
          </a>
          {COMMIT_HASH && (
            <a
              className={styles.link}
              href={`${REPO_URL}/commit/${COMMIT_HASH}`}
              target="_blank"
              rel="noopener noreferrer"
            >
              {COMMIT_HASH.slice(0, 7)}
            </a>
          )}
        </span>
        <Divider orientation="vertical" className={styles.divider} />
        <span className={styles.group}>
          <span className={styles.label}>
            <img
              src="/gov.ico"
              alt=""
              width={14}
              height={14}
              style={{ marginRight: 4, verticalAlign: '-2px' }}
            />
          </span>
          <a
            className={styles.link}
            href="https://beian.mps.gov.cn/#/query/webSearch?code=50010602504893"
            target="_blank"
            rel="noopener noreferrer"
          >
            渝公网安备50010602504893号
          </a>
        </span>
        <Divider orientation="vertical" className={styles.divider} />
        <span className={styles.group}>
          <span className={styles.label}></span>
          <a
            className={styles.link}
            href="https://beian.miit.gov.cn/"
            target="_blank"
            rel="noopener noreferrer"
          >
            渝ICP备2024030252号
          </a>
        </span>
      </div>
    </div>
  );
};

export default Footer;
