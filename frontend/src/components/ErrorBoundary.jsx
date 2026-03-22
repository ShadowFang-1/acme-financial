import React from 'react';
import { ShieldAlert } from 'lucide-react';

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error("[Antigravity] Crash caught by ErrorBoundary:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen bg-[#0f172a] flex flex-col items-center justify-center p-6 text-center text-white">
          <div className="w-20 h-20 bg-red-500/20 rounded-3xl flex items-center justify-center mb-8 shadow-2xl shadow-red-500/10">
            <ShieldAlert size={40} className="text-red-500" />
          </div>
          <h1 className="text-3xl font-black mb-4 tracking-tighter uppercase italic">System Fault Detected</h1>
          <p className="text-slate-400 max-w-md mb-10 font-medium">
            The ACME interface encountered a critical runtime error. This usually happens during connection shifts or session expiration.
          </p>
          <div className="flex gap-4">
            <button 
              onClick={() => window.location.href = '/'}
              className="px-8 py-4 bg-primary border-2 border-white/10 rounded-2xl font-black uppercase text-xs tracking-widest hover:bg-white/5 transition-all"
            >
              Return Home
            </button>
            <button 
              onClick={() => window.location.reload()}
              className="px-8 py-4 bg-secondary text-primary rounded-2xl font-black uppercase text-xs tracking-widest hover:scale-[1.05] transition-all"
            >
              Force Refresh
            </button>
          </div>
          {process.env.NODE_ENV === 'development' && (
            <pre className="mt-12 p-6 bg-black/40 rounded-3xl text-xs text-red-400 text-left max-w-2xl overflow-auto border border-white/5 opacity-50">
              {this.state.error?.toString()}
            </pre>
          )}
        </div>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
