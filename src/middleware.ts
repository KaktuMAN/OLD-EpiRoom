import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl
  if (pathname === '/') {
    return NextResponse.redirect(new URL('/floors/0', request.nextUrl))
  }
  if (pathname === '/EpiRoom/') {
    return NextResponse.redirect(new URL('/floors/0', request.nextUrl))
  }
}