import type { ReactNode } from 'react';

interface GradientBackgroundProps {
    children: ReactNode;
}

function GradientBackground({ children }: GradientBackgroundProps) {
    return (
        <div
            style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                height: '100vh',
                background: 'linear-gradient(135deg, springgreen 0%, aqua 100%)',
            }}
        >
            {children}
        </div>
    );
}

export default GradientBackground;
