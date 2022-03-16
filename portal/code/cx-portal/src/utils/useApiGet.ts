import { useState, useEffect } from "react"

export type TApiResponse = {
  status: number
  statusText: string
  data: string
  error: unknown
  loading: boolean
}

/**
 * useApiGet - a simple hook to retrieve static data asynchronously in cases
 * where we don't want to store the result in redux
 * 
 * @param url 
 * @returns apiResponse
 */

export const useApiGet = (url: string): TApiResponse => {
  const [status, setStatus] = useState<number>(0)
  const [statusText, setStatusText] = useState<string>('')
  const [data, setData] = useState<string>('')
  const [error, setError] = useState<unknown>()
  const [loading, setLoading] = useState<boolean>(false)

  const getAPIData = async () => {
    setLoading(true)
    try {
      // Note that we are not using the Redux store intentionally,
      // because this is not a Catena-X core data set.
      const apiResponse = await fetch(url)
      const text = await apiResponse.text()
      setStatus(apiResponse.status)
      setStatusText(apiResponse.statusText)
      setData(text)
    } catch (error) {
      setError(error)
    }
    setLoading(false)
  }

  useEffect(() => {
    getAPIData()
  }, [])

  return { status, statusText, data, error, loading }
}
