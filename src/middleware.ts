import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl

  if (!pathname.match(/\/floors\/[0-3]/g)) {
    return NextResponse.redirect(new URL('/floors/0', request.url))
  }
}

export const config = {
  matcher: ['/floors/:path*', '/'],
}